package com.example.tx.hexagonal.port;

public interface PaymentPort {
    void processPayment(double amount, String paymentInfo);
}
