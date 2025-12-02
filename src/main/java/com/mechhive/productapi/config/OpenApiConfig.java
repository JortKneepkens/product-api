package com.mechhive.productapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productApiSpec() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product API")
                        .description("Product and transaction service with caching and currency enrichment")
                );
    }
}
