package com.example.di;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyService {
    private final Optional<AnotherService> anotherService;

    public MyService(Optional<AnotherService> anotherService) {
        this.anotherService = anotherService;
    }

    public boolean hasAnotherService() {
        return anotherService.isPresent();
    }
}
