package com.example.vocatest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://vocalist.kro.kr", description = "vocalist api swagger")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
                .type(SecurityScheme.Type.OAUTH2)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");


        return new OpenAPI()
                .info(new Info()
                        .title("단어장 API")
                        .description("단어장 관련 API")
                        .version("1.0.0"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

//    @Bean
//    public OperationCustomizer globalHeader() {
//        return (operation, handlerMethod) -> {
//            operation.addParametersItem(new Parameter()
//                    .in(ParameterIn.HEADER.toString())
//                    .schema(new StringSchema().name("Refresh-Token"))
//                    .name("Refresh-Token"));
//            return operation;
//        };
//    }
}
