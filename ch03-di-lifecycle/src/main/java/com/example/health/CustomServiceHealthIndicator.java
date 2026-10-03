package com.example.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomServiceHealthIndicator implements HealthIndicator {

    private final MyCriticalBusinessService myCriticalBusinessService;

    public CustomServiceHealthIndicator(MyCriticalBusinessService myCriticalBusinessService) {
        this.myCriticalBusinessService = myCriticalBusinessService;
    }

    @Override
    public Health health() {
        if (myCriticalBusinessService.isOperational()) {
            return Health.up().withDetail("reason", "MyCriticalBusinessService is operational").build();
        } else {
            return Health.down().withDetail("error", "MyCriticalBusinessService is not responding").build();
        }
    }
}
