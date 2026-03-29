package com.zapter.zapter_backend;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class ZapterBackendApplication implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))

                .info(new Info()
                        .title("Zapter API")
                        .version("1.0")
                        .description("API documentation for Zapter App")
                        .contact(new Contact()
                                .name("Shaikh Masir")
                                .email("send2masir@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/MohammadMasir/zapter"));

    }

    public static void main(String[] args) {
        SpringApplication.run(ZapterBackendApplication.class, args);
    }

}
