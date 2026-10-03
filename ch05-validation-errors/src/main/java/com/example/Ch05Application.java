package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// com.example.legacy.errorhandling is not scanned: only one of the two global handlers should be active.
@SpringBootApplication(scanBasePackages = {"com.example.api", "com.example.errorhandling"})
public class Ch05Application {

    public static void main(String[] args) {
        SpringApplication.run(Ch05Application.class, args);
    }
}
