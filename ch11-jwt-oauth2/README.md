# Chapter 11 – JWT, OAuth2, and Identity in Distributed Systems

| Book section | Code |
|---|---|
| JWT resource server (JWKS from Keycloak) | `jwt/config/JwtResourceServerConfig.java`, `application.properties` |
| Reading claims | `jwt/controller/ProtectedController.java` |
| Internal HMAC-signed JWTs (JJWT) | `jwt/internal/JwtGenerator.java`, `InternalJwtResourceServerConfig.java` |
| OAuth2/OIDC login (Okta, Keycloak) | `oauth2/config/OAuth2LoginConfig.java`, `oauth2/controller/UserInfoController.java`, `application-oauth2-login.properties` |

## Changes from the book
- **JJWT 0.12+ API:** `Keys.secretKeyFor(SignatureAlgorithm.HS256)`, `setSubject()` and similar methods are deprecated.
  The key is now `Jwts.SIG.HS256.key().build()`, typed as `SecretKey`. The book's `Key` type would not compile
  when passed to `NimbusJwtDecoder.withSecretKey()`.
- **Bug fix:** `InternalJwtResourceServerConfig` created its own `new JwtGenerator()`, which generates a random key,
  so tokens from any other generator instance could never be validated. `JwtGenerator` is now a shared bean.
- The generator writes the `roles` claim (the book used `role`), which `ProtectedController` reads.
- Okta/Keycloak client settings live in an `oauth2-login` profile, because `issuer-uri` contacts the provider at startup.
  The tests use explicit endpoints, so no identity provider is needed.
