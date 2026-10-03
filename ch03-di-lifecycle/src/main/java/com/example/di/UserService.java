package com.example.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    private final NotificationService notificationService;

    // Injects EmailNotificationService due to @Qualifier.
    // Note: with constructor injection the @Qualifier must sit on the constructor parameter;
    // placing it on the field (as in the book) is ignored and the @Primary bean would be injected.
    public UserService(@Qualifier("emailNotificationService") NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String notifyUser(String message) {
        return notificationService.send(message);
    }
}
