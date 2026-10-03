package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The versioning examples (com.example.api.versioning.*) each declare an alternative "ProductController"
// for the same resource, so they are not scanned here and are exercised individually in the tests.
@SpringBootApplication(scanBasePackages = {"com.example.api.product", "com.example.api.order"})
public class Ch04Application {

    public static void main(String[] args) {
        SpringApplication.run(Ch04Application.class, args);
    }
}
