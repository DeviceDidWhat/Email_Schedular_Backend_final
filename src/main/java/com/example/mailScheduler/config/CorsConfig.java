package com.example.mailScheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer
{

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")  // Allow CORS for all endpoints
                .allowedOrigins("http://localhost:3000")  // Allow React app to make requests
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow necessary HTTP methods
                .allowCredentials(true)  // Allow sending cookies if needed
                .maxAge(3600);  // Cache the CORS configuration for 1 hour
    }
}