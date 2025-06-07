package com.assessment.position.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String[] allowedHeaders;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow specified origins
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        
        // Allow specified methods
        config.setAllowedMethods(Arrays.asList(allowedMethods));
        
        // Allow all headers
        config.setAllowedHeaders(Arrays.asList(allowedHeaders));
        
        // Allow credentials (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);
        
        // How long the response to preflight requests can be cached
        config.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
} 