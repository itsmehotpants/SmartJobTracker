package com.jobtracker.demo.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Get request details
        String method = "UNKNOWN";
        String uri = "UNKNOWN";
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                method = request.getMethod();
                uri = request.getRequestURI();
            }
        } catch (Exception ignored) {}

        // Sanitize arguments (hide passwords)
        String args = Arrays.stream(joinPoint.getArgs())
                .map(arg -> {
                    if (arg == null) return "null";
                    String str = arg.toString();
                    if (str.toLowerCase().contains("password")) {
                        return "[REDACTED]";
                    }
                    return arg.getClass().getSimpleName();
                })
                .collect(Collectors.joining(", "));

        logger.info("[API] → {} {} | Args: [{}]", method, uri, args);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("[API] ← {} {} | {}ms | SUCCESS", method, uri, duration);
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("[API] ✗ {} {} | {}ms | ERROR: {}", method, uri, duration, ex.getMessage());
            throw ex;
        }
    }
}
