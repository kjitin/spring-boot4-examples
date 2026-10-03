package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// com.example.circular is deliberately NOT scanned: it demonstrates a circular dependency that fails startup.
@SpringBootApplication(scanBasePackages = {
        "com.example.di", "com.example.scopes", "com.example.lifecycle",
        "com.example.failfast", "com.example.health"})
public class Ch03Application {

    public static void main(String[] args) {
        SpringApplication.run(Ch03Application.class, args);
    }
}
