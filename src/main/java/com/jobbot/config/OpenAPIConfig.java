package com.jobbot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Job Finder & Auto-Application Assistant API")
                        .version("1.0.0")
                        .description("""
                                Backend API for AI-powered job finding and auto-application system.
                                
                                ## Features:
                                - User management and authentication
                                - Job search and matching
                                - Resume management
                                - AI-powered application assistance
                                - Auto-application workflow
                                - Application tracking
                                
                                ## Authentication:
                                Currently using basic authentication. JWT tokens coming soon.
                                """)
                        .contact(new Contact()
                                .name("Visionary Ventures")
                                .email("support@visionaryventures.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.visionaryventures.com")
                                .description("Production Server")
                ));
    }
}

