# Chapter 17 – Building Microservices with Spring Boot

| Module | Book topic | Port |
|---|---|---|
| `eureka-server` | Service registry (`@EnableEurekaServer`) | 8761 |
| `config-server` | Spring Cloud Config Server (Git; `native` profile serves `config-repo/`) | 8888 |
| `product-service` | Eureka client + Config client | 8082 |
| `order-service` | Consul discovery/config, Resilience4j circuit breaker, Kafka event producer | 8083 |
| `inventory-service` | Kafka event consumer | – |
| `api-gateway` | Spring Cloud Gateway with discovery routing, `StripPrefix`, circuit breaker + `FallbackController` | 8080 |

Run locally (each in its own terminal): `eureka-server`, `config-server` (`-Dspring-boot.run.profiles=native`),
`product-service`, `api-gateway`. Then `curl localhost:8080/api/products/`.
The tests need no infrastructure: Eureka/Consul are disabled, Kafka is replaced by the Spring Cloud Stream test binder,
and the gateway uses static discovery instances.

## Changes from the book
- **Spring Cloud Stream:** the annotation model (`@Output`, `@Input`, `@StreamListener`, binding interfaces) was removed
  in Spring Cloud Stream 4. The producer uses `StreamBridge`; the consumer is a `Consumer<String>` bean mapped to
  the book's `order-in` binding. Binding names and topics are unchanged.
- `bootstrap.yml` is replaced by `spring.config.import` (Config Server: `optional:configserver:`, Consul: `optional:consul:`).
- Resilience4j: `resilience4j-spring-boot4` 2.4.0 replaces `resilience4j-spring-boot3`.
- Gateway 5 properties moved to `spring.cloud.gateway.server.webflux.*`. The book's `FallbackController` was pasted
  inside the YAML block; it is now a Java class.
- Spring Cloud release train `2025.1.x` (the train for Boot 4.0).
