package com.jobtracker.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Smart Job Tracker API",
        version = "1.0.0",
        description = """
            **Production-Ready SaaS Backend for Job Tracking**
            
            Features:
            - 🔐 JWT Authentication with Refresh Token Rotation
            - 👥 Role-Based Access Control (USER / ADMIN)
            - 💼 Job Discovery, Search & Advanced Filtering
            - 🔖 Bookmark Management with CSV Export
            - 📋 Application Tracker with Pipeline Status
            - 📊 Analytics Dashboard
            - ⏰ Scheduled Job Expiry
            
            **Authentication:** Use the `/auth/login` endpoint to get a JWT token,
            then click the 'Authorize' button above and enter: `Bearer <your-token>`
            """,
        contact = @Contact(
            name = "Smart Job Tracker Team",
            email = "support@jobtracker.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Development"),
        @Server(url = "https://api.jobtracker.com", description = "Production")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Bearer token authentication. Get your token from /auth/login",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
