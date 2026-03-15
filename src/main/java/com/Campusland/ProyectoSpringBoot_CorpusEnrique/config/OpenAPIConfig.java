package com.Campusland.ProyectoSpringBoot_CorpusEnrique.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class openAPIConfig {

@Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(new Info()
                .title("API DOCUMENTADA CAMPUSLAND CON VENTA, DETALLE VENTA, PRODUCTO")
                .version("1.0")
                .description("Esta api se construyo de forma academica para practicar swagger"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));

    }
}
