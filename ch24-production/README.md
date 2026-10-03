# Chapter 24 – Production Concerns in the Cloud

| Book section | Code |
|---|---|
| JSON logs for log aggregation | `logback-spring.xml` |
| CloudWatch metrics | `CloudWatchMetricsConfig.java`, `application-aws.properties` |
| AWS X-Ray tracing | `XRayTracingConfig.java`, `application-aws.properties` |

Run with `--spring.profiles.active=aws`. AWS credentials and region come from the default AWS provider chain.

## Changes from the book
- `micrometer-registry-cloudwatch` (AWS SDK v1) ended at Micrometer 1.12; this uses `micrometer-registry-cloudwatch2`.
  Spring Boot has no CloudWatch auto-configuration (the book's `management.metrics.export.cloudwatch.*` properties
  came from Spring Cloud AWS 2.x). `CloudWatchMetricsConfig` registers the registry and keeps those property names.
- **`io.opentelemetry:opentelemetry-exporter-aws-xray` and `opentelemetry-exporter-gcp` do not exist.** X-Ray receives
  OpenTelemetry spans through the ADOT collector over OTLP. The app only needs X-Ray-compatible trace IDs
  (`io.opentelemetry.contrib:opentelemetry-aws-xray`). For Google Cloud Trace, the real artifact is
  `com.google.cloud.opentelemetry:exporter-trace` (shown, commented out, in `pom.xml`).
- Tests use a mocked `CloudWatchAsyncClient`, so no AWS account is needed.
