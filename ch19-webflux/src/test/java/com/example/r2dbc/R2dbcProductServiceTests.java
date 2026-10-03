package com.example.r2dbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class R2dbcProductServiceTests {

    @Autowired
    @Qualifier("r2dbcProductService")
    ProductService productService;

    @Test
    void reactiveRepositoryOperations() {
        // schema.sql + data.sql seed two products
        StepVerifier.create(productService.searchProducts("Reactive").map(Product::getName))
                .expectNext("Reactive Laptop", "Reactive Mouse")
                .verifyComplete();

        StepVerifier.create(productService.saveProduct(new Product(null, "Reactive Keyboard", 80.0))
                        .flatMap(saved -> productService.getProductById(saved.getId())))
                .expectNextMatches(p -> p.getName().equals("Reactive Keyboard") && p.getId() != null)
                .verifyComplete();
    }
}
