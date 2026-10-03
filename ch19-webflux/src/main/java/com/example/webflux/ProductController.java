package com.example.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/products/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
        return productService.findProductById(id);
    }

    @GetMapping(value = "/products/stream", produces = "text/event-stream")
    public Flux<Product> streamAllProducts() {
        return productService.findAllProducts()
                .delayElements(Duration.ofSeconds(1)); // Simulate streaming data over time
    }
}
