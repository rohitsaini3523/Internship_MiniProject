package com.example.Backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UserConfiguration {
    @Bean
    @Primary
    public OpenAPI userOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Login and Registration API")
                        .description("Endpoint for user login and Registration"));
    }

}
