package com.example.tracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final Tracer tracer;

    public ProductService(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getProductDetails(String productId) {
        // Create a new span for this operation
        Span span = tracer.spanBuilder("getProductDetails").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("product.id", productId);
            span.addEvent("Fetching product from database");
            // Simulate database call
            Thread.sleep(50);
            span.addEvent("Product fetched");
            return "Details for product " + productId;
        } catch (InterruptedException e) {
            span.recordException(e);
            Thread.currentThread().interrupt();
            return "Error fetching product";
        } finally {
            span.end();
        }
    }
}
