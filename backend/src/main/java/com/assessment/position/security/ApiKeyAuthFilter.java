package com.assessment.position.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String apiKey;
    private RequestMatcher excludedUrls;

    public ApiKeyAuthFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setExcludeUrls(String... urls) {
        List<RequestMatcher> matchers = Arrays.stream(urls)
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toList());
        this.excludedUrls = new OrRequestMatcher(matchers);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedUrls != null && excludedUrls.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey.equals(requestApiKey)) {
            Authentication authentication = new ApiKeyAuthentication(apiKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid API Key");
        }
    }
} 