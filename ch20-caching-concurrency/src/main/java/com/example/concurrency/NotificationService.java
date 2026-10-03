package com.example.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Async("taskExecutor") // Specify the executor bean name
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to {} with subject '{}' in thread {}", to, subject, Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // Simulate network delay for sending email
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Email sent to {} in thread {}", to, Thread.currentThread().getName());
    }

    @Async
    public CompletableFuture<String> sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to {} with message '{}' in thread {}", phoneNumber, message, Thread.currentThread().getName());
        try {
            Thread.sleep(1500); // Simulate network delay for sending SMS
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("SMS sent to {} in thread {}", phoneNumber, Thread.currentThread().getName());
        return CompletableFuture.completedFuture("SMS sent successfully to " + phoneNumber);
    }
}
