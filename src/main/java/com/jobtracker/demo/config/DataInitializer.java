package com.jobtracker.demo.config;

import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.entity.enums.Role;
import com.jobtracker.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Admin user if not exists
            if (!userRepository.existsByEmail("admin@jobtracker.com")) {
                User admin = User.builder()
                        .name("Admin User")
                        .email("admin@jobtracker.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .phone("+1-555-0100")
                        .skills("Java, Spring Boot, System Administration")
                        .bio("Platform administrator with full access to job management.")
                        .build();
                userRepository.save(admin);
                logger.info("✅ Admin user created: admin@jobtracker.com / admin123");
            }

            // Create Demo user if not exists
            if (!userRepository.existsByEmail("user@jobtracker.com")) {
                User user = User.builder()
                        .name("John Doe")
                        .email("user@jobtracker.com")
                        .password(passwordEncoder.encode("user123"))
                        .role(Role.USER)
                        .phone("+1-555-0200")
                        .skills("Python, React, Machine Learning")
                        .bio("Software engineer looking for exciting opportunities in tech.")
                        .build();
                userRepository.save(user);
                logger.info("✅ Demo user created: user@jobtracker.com / user123");
            }
        };
    }
}
