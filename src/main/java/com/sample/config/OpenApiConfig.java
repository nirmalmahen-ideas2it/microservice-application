package com.sample.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Keycloak";

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "keycloak";

        return new OpenAPI()
            .info(new Info().title("User and Role Module").version("v1"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components().addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl("http://localhost:7070/realms/usersApp-relam/protocol/openid-connect/auth")
                            .tokenUrl("http://localhost:7070/realms/usersApp-relam/protocol/openid-connect/token")
                            .scopes(new Scopes().addString("openid", "OpenID Connect scope"))
                        )
                    )
            ));
    }

}
