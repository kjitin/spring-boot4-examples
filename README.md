# Spring Boot 4 in Java – Book Examples

Runnable code for every code listing in *Spring Boot 4 in Java: A Comprehensive Guide*, organised by chapter.
Each chapter is a standalone Maven project on **Spring Boot 4.0.8** (Spring Framework 7, Spring Security 7,
Jakarta EE 11, Jackson 3) and, where needed, **Spring Cloud 2025.1.3**. Every chapter has tests that run the
snippets, not just compile them.

## Chapters

| Chapter | Directory | What's inside |
|---|---|---|
| 2 – Spring Boot 4 Under the Hood | [`ch02-boot-internals`](ch02-boot-internals) | Custom auto-configuration, `@ConfigurationProperties`, profiles, Actuator |
| 3 – DI and Bean Lifecycles | [`ch03-di-lifecycle`](ch03-di-lifecycle) | Injection gotchas, scopes, lifecycle callbacks, fail-fast checks, health indicators |
| 4 – Advanced REST API Design | [`ch04-rest-api-design`](ch04-rest-api-design) | CRUD API, 5 versioning strategies, idempotency keys |
| 5 – Validation & Problem Details | [`ch05-validation-errors`](ch05-validation-errors) | Validation groups, composed constraints, RFC 7807 |
| 6 – Files, Streaming, Large Payloads | [`ch06-files-streaming`](ch06-files-streaming) | Upload/download with range requests, JSON/NDJSON/CSV streaming, async MVC |
| 7 – Spring Data JPA | [`ch07-spring-data-jpa`](ch07-spring-data-jpa) | N+1 and its fixes, projections, query hints, native queries |
| 8 – Transactions | [`ch08-transactions`](ch08-transactions) | Propagation, isolation, optimistic/pessimistic locking, layered and hexagonal boundaries |
| 9 – JDBC, jOOQ, NoSQL | [`ch09-jdbc-jooq-nosql`](ch09-jdbc-jooq-nosql) | `JdbcTemplate`, batching, jOOQ codegen, MongoDB, Redis |
| 10 – Spring Security | [`ch10-security`](ch10-security) | Filter chains, method security, permission evaluator, CORS, session fixation |
| 11 – JWT & OAuth2 | [`ch11-jwt-oauth2`](ch11-jwt-oauth2) | Resource server, JJWT-signed internal tokens, OIDC login |
| 12 – Securing Microservices | [`ch12-microservice-security`](ch12-microservice-security) | Gateway rate limiting, client-credentials `WebClient`, field encryption, Vault, audit |
| 13 – Testing | [`ch13-testing`](ch13-testing) | Slices, Mockito beans, Testcontainers, Selenium & Playwright E2E |
| 14 – Code Quality | [`ch14-code-quality`](ch14-code-quality) | Checkstyle, SpotBugs, PMD/CPD, Sonar, refactorings |
| 15 – Observability | [`ch15-observability`](ch15-observability) | JSON logs + MDC, Micrometer/Prometheus, OpenTelemetry tracing |
| 17 – Microservices Core Patterns | [`ch17-microservices`](ch17-microservices) | Eureka, Config Server, Consul, Resilience4j, Gateway, Kafka events |
| 18 – Spring Cloud Function | [`ch18-cloud-function`](ch18-cloud-function) | Functions over HTTP, composition, AWS Lambda packaging |
| 19 – WebFlux & Reactor | [`ch19-webflux`](ch19-webflux) | Reactive controllers, functional endpoints, Reactor operators, backpressure, R2DBC |
| 20 – Caching & Concurrency | [`ch20-caching-concurrency`](ch20-caching-concurrency) | Caffeine/Redis caching, `@Async` executors, `CompletableFuture`, Hikari |
| 21 – Performance Testing | [`ch21-performance-testing`](ch21-performance-testing) | Gatling simulation (targets Chapter 19), JMeter/JFR commands |
| 22 – Deployment | [`ch22-deployment`](ch22-deployment) | Multi-stage Dockerfile, Kubernetes Deployment/Service/Ingress |
| 23 – GraalVM Native Images | [`ch23-native-image`](ch23-native-image) | Native build (local or buildpacks), native K8s deployment |
| 24 – Production in the Cloud | [`ch24-production`](ch24-production) | JSON logging, CloudWatch metrics, X-Ray-compatible tracing |
| 25 – Migrating Boot 3 → 4 | [`ch25-migration`](ch25-migration) | Maven and Gradle setup, virtual threads |
| 26 – Design Patterns & Architecture | [`ch26-hexagonal`](ch26-hexagonal) | Ports and adapters |

Chapters 1, 16 and 27 contain no code listings.

Each chapter's README maps the book's snippets to files and lists **every change made to the book's code and why**.

## Building

Requirements: JDK 21+ (tested on JDK 26). Docker is optional.

```bash
./mvnw verify                       # everything
./mvnw verify -pl ch10-security     # one chapter
cd ch19-webflux && ../mvnw spring-boot:run
```

- **Docker:** Testcontainers tests (MongoDB, Redis, PostgreSQL, Vault) run when Docker is available and are skipped otherwise.
- **Browser E2E tests** (Chapter 13) are opt-in: `./mvnw verify -pl ch13-testing -Pe2e`.
- **Native image** (Chapter 23): `../mvnw -Pnative spring-boot:build-image` (Docker only) or `native:compile` (GraalVM 25+).
- **Load test** (Chapter 21): start Chapter 19, then `./mvnw gatling:test -pl ch21-performance-testing`.

## How the code was verified

- `./mvnw clean verify` from the root, with Docker running: all chapters build and all tests pass.
- Chapter 13 Selenium + Playwright E2E tests run with `-Pe2e`.
- Chapter 21: the Gatling simulation ran against the running Chapter 19 app (20 requests, 0 failures).
- Chapter 22: the Dockerfile builds, and the container serves `/actuator/health/{liveness,readiness}`.
  All Kubernetes manifests pass `kubeconform -strict`.
- Chapter 23: native image built with Paketo buildpacks and run. It starts in under 0.1 s and uses about 43 MiB.
- Chapter 18: `-Paws` produces a flat Lambda jar; the test invokes the real AWS `FunctionInvoker`.
- Chapter 25: built with both Maven and Gradle 9.8.

## Spring Boot 4 changes you will meet in the book's code

The book was written against earlier milestones, and several snippets use APIs that Spring Boot 4 / Spring Cloud 2025 removed:

| Book code | Spring Boot 4 / current |
|---|---|
| `@MockBean`, `@SpyBean` | `@MockitoBean`, `@MockitoSpyBean` |
| `org.springframework.boot.test.web.client.TestRestTemplate` | `spring-boot-resttestclient` + `@AutoConfigureTestRestTemplate` |
| `...test.autoconfigure.web.servlet.WebMvcTest`, `...orm.jpa.DataJpaTest` | `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`, `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest` |
| `org.springframework.boot.actuate.health.HealthIndicator` | `org.springframework.boot.health.contributor.HealthIndicator` |
| `com.fasterxml.jackson.databind.ObjectMapper` | Jackson 3: `tools.jackson.databind.ObjectMapper` |
| `spring-boot-starter-web` | `spring-boot-starter-webmvc` (modular starters, each with a `-test` companion) |
| Spring Cloud Stream `@Output`/`@Input`/`@StreamListener` | `StreamBridge` and functional `Consumer`/`Function` beans |
| `spring-cloud-starter-gateway`, `spring.cloud.gateway.routes` | `spring-cloud-starter-gateway-server-webflux`, `spring.cloud.gateway.server.webflux.routes` |
| `bootstrap.yml` / `bootstrap.properties` | `spring.config.import=configserver:` / `consul:` / `vault://` |
| `management.zipkin.tracing.endpoint` | `management.tracing.export.zipkin.endpoint` |
| `resilience4j-spring-boot3` | `resilience4j-spring-boot4` |
| JJWT `Keys.secretKeyFor(SignatureAlgorithm.HS256)` | `Jwts.SIG.HS256.key().build()` |
| `spring-native` (Boot 2.x) | Built-in AOT; native images need GraalVM 25+ |
