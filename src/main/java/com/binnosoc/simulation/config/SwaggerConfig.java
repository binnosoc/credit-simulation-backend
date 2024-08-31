package com.binnosoc.simulation.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-config.json")
                .addResourceLocations("classpath:/static/swagger-config.json");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Authentication API")
                .version("v1")
                .description("This API provides endpoints for user registration, authentication, " +
                        "account activation, and password reset. It handles user authentication " +
                        "using JWT tokens and provides secure access to protected resources. " +
                        "All operations are protected by a JWT-based authentication system. " +
                        "Users can register, authenticate, reset their passwords, and activate " +
                        "their accounts through this API.")
                .contact(new Contact()
                    .name("OUSSENI BORO")
                    .url("https://binnosoc.com/")
                    .email("ousseni.boro@binnosoc.com")))
          
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/**")
            .build();
    }
}


