# Chapter 15 – Observability: Logging, Metrics, Tracing

| Book section | Code |
|---|---|
| Structured JSON logging (logstash encoder) | `logback-spring.xml` |
| MDC correlation | `logging/OrderService.java` (+ `plain-logs` profile with the book's `%X{orderId}` pattern) |
| Micrometer + Prometheus | `metrics/PaymentService.java`, `application.properties` |
| OpenTelemetry tracing + Zipkin | `tracing/ProductService.java`, `tracing/TracingConfig.java` |
| Health details | `application.properties` |

Try it: `../mvnw spring-boot:run`, then `POST /payments?amount=10` and `GET /actuator/prometheus`.

## Changes from the book
- The book lists raw OpenTelemetry SDK/exporter jars. Spring Boot 4 has `spring-boot-starter-opentelemetry` and
  `spring-boot-starter-zipkin`. Added `TracingConfig`, because Boot does not expose an OpenTelemetry API `Tracer` bean.
- `management.zipkin.tracing.endpoint` was removed; it is now `management.tracing.export.zipkin.endpoint`.
- **Bug fix:** `management.tracing.sampling.probability=1.0 # Sample all traces`. In `.properties` files an inline
  `#` is part of the value, so it would not parse. Comments are now on their own lines.
- `logback.xml` included `base.xml`, which adds a plain-text console appender, so every line was logged twice. It now includes `defaults.xml`.
- With `publishPercentileHistogram()`, the Prometheus registry ignores `publishPercentiles(...)` and exports buckets.
  Compute p95 in PromQL: `histogram_quantile(0.95, rate(payment_processing_time_seconds_bucket[5m]))`.
