package com.assessment.position.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String apiKey;

    public ApiKeyAuthentication(String apiKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_API")));
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "API";
    }
} 