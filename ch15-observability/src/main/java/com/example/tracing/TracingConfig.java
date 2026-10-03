package com.example.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Not in the book: Spring Boot auto-configures the OpenTelemetry SDK but not an OpenTelemetry API Tracer bean,
// which ProductService injects.
@Configuration
public class TracingConfig {

    @Bean
    public Tracer otelTracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("com.example.tracing");
    }
}
