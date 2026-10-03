package com.example.di;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SmsNotificationService implements NotificationService {
    @Override
    public String send(String message) {
        return "SMS: " + message;
    }
}
