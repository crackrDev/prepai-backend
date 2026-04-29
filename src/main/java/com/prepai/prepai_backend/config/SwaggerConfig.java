package com.prepai.prepai_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI prepAiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PrepAI Backend API")
                        .description("AI Mock Interview Platform — " +
                                "Backend REST API for PrepAI. " +
                                "Built with Spring Boot + MySQL + GPT-4.")

                                .version("v1.0")
                                .contact(new Contact()
                                        .name("prepAI Dev Team")
                                        .email("prepai@dev.com")
                                )

                                .license(new License()
                                        .name("MIT License")
                                )
                        )
                //JWT Auth Support in Swagger UI
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your supabase token here") //We have to enter our supabase token here
                        )
                );
    }
}
