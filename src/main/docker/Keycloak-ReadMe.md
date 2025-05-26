# 🛡️ JHipster Microservice with Keycloak JWT Authentication

This project is a JHipster-generated Spring Boot microservice that uses **Keycloak** as the identity provider and **JWT
tokens** for securing APIs.

## 📦 Technologies Used

- Spring Boot (JHipster-generated)
- Spring Security OAuth2 Resource Server
- Keycloak 24.0.2
- JWT Token Authentication
- Swagger UI with OAuth2
- Docker (for Keycloak)

---

## 🐳 1. Start Keycloak via Docker

Create a `docker-compose.yml` file to run Keycloak on port 7070:

```yaml
version: '3.8'

services:
  keycloak:
    image: quay.io/keycloak/keycloak:24.0.2
    container_name: keycloak
    ports:
      - "7070:8080"
    environment:
      - KEYCLOAK_ADMIN=admin
      - KEYCLOAK_ADMIN_PASSWORD=admin
      - KC_HTTP_ENABLED=true
      - KC_HOSTNAME_STRICT=false
    command: [ "start-dev" ]
```

Start it:

```bash
docker compose up -d
```

---

## 🏰 2. Configure Keycloak

### Create a Realm

1. Go to [http://localhost:7070](http://localhost:7070)
2. Login:

- Username: `admin`
- Password: `admin`

3. Click the realm dropdown → **Create Realm**
4. Enter a name (e.g., `myapp-realm`) and click **Create**

### Create a Confidential Client

1. Go to **Clients** → **Create Client**
2. Configure:

- **Client ID**: `myapp-client`
- **Protocol**: `openid-connect`
- **Client Type**: `Confidential`

3. Click **Next**
4. On **Capability Config**:

- Enable **Standard Flow**
- Enable **Direct Access Grants** (optional)

5. Click **Next**
6. On **Login Settings**:

- **Valid Redirect URIs**: `http://localhost:9090/*`
- **Web Origins**: `+`

7. Click **Save**
8. Go to the **Credentials** tab → Copy the **Client Secret**

---

## 🔐 Application Configuration

### `application.yml`

```yaml
spring:
  application:
    name: user-application

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${jwt.issuerUri}

springdoc:
  swagger-ui:
    oauth2-redirect-url: ${oauth.redirectUri}
    oauth:
      client-id: ${oauth.clientId}
      client-secret: ${oauth.clientSecret}  # Only required for confidential clients
      authorization-url: ${oauth.authUri}
      token-url: ${oauth.tokenUri}
      scope: openid

server:
  port: 9090
```

---

## 🔒 Spring Security Configuration

```java

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 📘 Swagger (OpenAPI) Configuration

Note: The swagger is configured to use the `authorization_code` flow, which requires a client secret. This is not
recommended for public clients.
The swagger in a production level should be config to general JWT Auth flow.

```java

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Keycloak";

    @Value("${oauth.authUri}")
    private String authorizationUrl;

    @Value("${oauth.tokenUri}")
    private String tokenUrl;

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
                                                .authorizationUrl(authorizationUrl)
                                                .tokenUrl(tokenUrl)
                                                .scopes(new Scopes().addString("openid", "OpenID Connect scope"))
                                        )
                                )
                ));
    }
}
```

---

## 🧪 Authenticate via Postman (Instead of Swagger)

You can generate a token using `client_credentials` flow directly:

### Request

```http
POST http://localhost:7070/realms/myapp-realm/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

client_id=myapp-client
client_secret=<your-client-secret>
grant_type=client_credentials
```

### Use the token

Use the `access_token` received in your request headers:

```http
Authorization: Bearer <access_token>
```

---

## 🔄 Inter-Service Communication

If another microservice wants to call this app:

1. Register it in Keycloak as a **confidential client**
2. Use `client_credentials` flow to get a JWT
3. Send the JWT as `Bearer` token in HTTP requests

This application will validate it using the configured `issuer-uri`.

---

## ✅ Summary

- Keycloak issues and signs the JWTs.
- This app only validates the JWTs via Spring Security.
- Swagger can be configured for authorization-code flow (requires client secret).
- Postman or other microservices can use `client_credentials` flow.
- Only clients registered in Keycloak can access protected endpoints.

---

## 📍 Note

If you don't want to expose the `client-secret` in Swagger (not recommended in production), use a public client with
PKCE or avoid Swagger-based login altogether and manually fetch tokens via Postman or your service logic.
