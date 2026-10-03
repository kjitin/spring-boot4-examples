# Chapter 12 – Securing Microservices

Two modules:

**`api-gateway`**: Spring Cloud Gateway with a Redis-backed `RequestRateLimiter` (`RateLimiterConfig`, `application.yml`).
`RateLimitingTests` sends a burst through the gateway and checks that some requests get `429`. Needs Docker; skipped without it.

**`secure-service`**
| Book section | Code |
|---|---|
| Service-to-service OAuth2 (client credentials) | `serviceclient/WebClientConfig.java`, `ProductServiceClient.java`, `application.properties` |
| Field-level encryption | `encryption/SensitiveDataConverter.java`, `AesEncryptor.java`, `UserProfile.java` |
| Secrets in Vault | `application-vault.properties` (`VaultSecretsTests` runs a real Vault in Docker) |
| Kubernetes secrets | `k8s/secret.yaml`, `k8s/deployment.yaml` |
| Security audit events | `audit/SecurityAuditListener.java` |

## Changes from the book
- Spring Cloud Gateway 5 moved its properties to `spring.cloud.gateway.server.webflux.*`, and the starter is now
  `spring-cloud-starter-gateway-server-webflux`. The key resolver is referenced as `#{@userKeyResolver}`.
- **Bug fix:** the `KeyResolver` called `.block()` inside `Mono.just(...)`. Blocking is not allowed on the gateway's
  event loop, so it now returns the reactive chain directly.
- **Bug fix:** `WebClientConfig` used `DefaultOAuth2AuthorizedClientManager`, which only works inside an HTTP request,
  and replaced the current user's `SecurityContext` with a fake token. It now uses
  `AuthorizedClientServiceOAuth2AuthorizedClientManager`, the manager intended for `client_credentials`. Also
  `oauth2Client.oauth2Client()` does not exist; the method is `oauth2Configuration()`.
- The product-service URL is configurable (`product-service.url`) so the client can be tested against a stub server.
- `AesEncryptor` is referenced but not shown in the book; an AES-GCM implementation is provided.
- Vault: `bootstrap.properties` and the `generic` backend are replaced by `spring.config.import=vault://` and the `kv` backend.
- The book's Kubernetes `Deployment` had no `spec.selector`, which `apps/v1` requires. Added it; both manifests pass `kubeconform -strict`.
