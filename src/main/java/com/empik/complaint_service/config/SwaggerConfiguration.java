package com.empik.complaint_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Complaint Service API")
                        .version("1.0")
                        .description("API documentation for Complaint Service")
                        .contact(new Contact()
                                .name("Empik Team")
                                .email("empik-support@gmail.com")));
    }
}
