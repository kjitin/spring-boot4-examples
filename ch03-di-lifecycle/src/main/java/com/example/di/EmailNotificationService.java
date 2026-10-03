package com.example.di;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements NotificationService {
    @Override
    public String send(String message) {
        return "Email: " + message;
    }
}
