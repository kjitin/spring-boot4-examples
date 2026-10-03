package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The default application is the JWT resource server (com.example.jwt.config).
// The internal-HMAC-token setup (com.example.jwt.internal) and the OAuth2 login setup (com.example.oauth2)
// are alternative SecurityFilterChain configurations; they are exercised individually in the tests.
@SpringBootApplication(scanBasePackages = {"com.example.jwt.config", "com.example.jwt.controller"})
public class Ch11Application {

    public static void main(String[] args) {
        SpringApplication.run(Ch11Application.class, args);
    }
}
