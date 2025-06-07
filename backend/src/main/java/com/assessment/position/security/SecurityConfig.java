package com.assessment.position.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${application.security.api-key}")
    private String apiKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter apiKeyFilter = new ApiKeyAuthFilter(apiKey);
        apiKeyFilter.setExcludeUrls(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/h2-console/**"
        );

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**", 
                    "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
} 