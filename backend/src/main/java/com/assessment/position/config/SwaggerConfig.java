package com.assessment.position.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod") // Active only when "prod" profile is NOT active
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "X-API-KEY";
        
        return new OpenAPI()
            .info(new Info()
                .title("Position Management API")
                .version("1.0")
                .description("API documentation for Position Management System")
                .contact(new Contact()
                    .name("Development Team")
                    .email("dev@example.com")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)));
    }
} 