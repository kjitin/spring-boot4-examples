package com.example.di;

import org.springframework.stereotype.Component;

@Component
public class OrderService {
    private final NotificationService notificationService;

    // Injects SmsNotificationService due to @Primary
    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String notifyCustomer(String message) {
        return notificationService.send(message);
    }
}
