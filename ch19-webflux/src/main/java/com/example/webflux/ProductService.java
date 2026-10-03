package com.example.webflux;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = Arrays.asList(
            new Product("1", "Laptop", 1200.0),
            new Product("2", "Mouse", 25.0),
            new Product("3", "Keyboard", 75.0)
    );

    public Flux<Product> findAllProducts() {
        return Flux.fromIterable(products);
    }

    public Mono<Product> findProductById(String id) {
        return Mono.justOrEmpty(products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst());
    }
}
