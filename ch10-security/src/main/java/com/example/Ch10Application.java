package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// com.example.securityvariants holds alternative SecurityFilterChain setups from the chapter.
// Only one filter-chain setup can be active at a time, so they are not scanned here;
// each one is loaded on its own in the tests.
@SpringBootApplication(scanBasePackages = {"com.example.security", "com.example.service", "com.example.web"})
public class Ch10Application {

    public static void main(String[] args) {
        SpringApplication.run(Ch10Application.class, args);
    }
}
