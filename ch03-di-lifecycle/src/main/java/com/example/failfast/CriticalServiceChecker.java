package com.example.failfast;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CriticalServiceChecker implements ApplicationRunner {

    private final ExternalService externalService;

    public CriticalServiceChecker(ExternalService externalService) {
        this.externalService = externalService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!externalService.isAvailable()) {
            throw new IllegalStateException("Critical external service is not available. Shutting down.");
        }
        System.out.println("Critical external service check passed.");
    }
}
