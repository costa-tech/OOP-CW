package com.example.ticketingsystem.config;

// Importing necessary Spring Framework annotations and classes
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Annotates this class as a configuration class for the Spring application
@Configuration
public class CorsConfig implements WebMvcConfigurer { // Implements the WebMvcConfigurer interface to customize MVC configurations

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Overrides the method to define CORS (Cross-Origin Resource Sharing) rules
        registry.addMapping("/**")
                // Configures CORS to apply to all endpoints in the application
                .allowedOrigins("http://localhost:3001", "http://localhost:3002")
                // Specifies the allowed origins (frontend URLs). Replace with production URLs if necessary.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Lists the HTTP methods permitted from the specified origins
                .allowedHeaders("*")
                // Allows all HTTP headers in requests from these origins
                .allowCredentials(true);
        // Indicates whether credentials (e.g., cookies, HTTP authentication) are allowed
    }
}
