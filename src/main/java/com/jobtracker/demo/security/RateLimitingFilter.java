package com.jobtracker.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.demo.dto.response.ApiResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> apiBuckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Bucket createNewApiBucket() {
        Bandwidth limit = Bandwidth.builder().capacity(100).refillGreedy(100, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket createNewAuthBucket() {
        Bandwidth limit = Bandwidth.builder().capacity(20).refillGreedy(20, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        
        Bucket bucket;
        if (path.startsWith("/auth/")) {
            bucket = authBuckets.computeIfAbsent(ip, k -> createNewAuthBucket());
        } else {
            bucket = apiBuckets.computeIfAbsent(ip, k -> createNewApiBucket());
        }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<?> apiResponse = ApiResponse.error(
                    "Too many requests. Please try again later.",
                    request.getRequestURI()
            );
            
            objectMapper.findAndRegisterModules();
            objectMapper.writeValue(response.getOutputStream(), apiResponse);
        }
    }
}
