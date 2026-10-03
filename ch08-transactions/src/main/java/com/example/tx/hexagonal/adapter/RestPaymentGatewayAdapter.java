package com.example.tx.hexagonal.adapter;

import com.example.tx.hexagonal.port.PaymentPort;
import org.springframework.stereotype.Component;

// Stand-in for a REST call to a payment provider: declines cards marked "DECLINE"
@Component
public class RestPaymentGatewayAdapter implements PaymentPort {

    @Override
    public void processPayment(double amount, String paymentInfo) {
        if ("DECLINE".equals(paymentInfo)) {
            throw new IllegalStateException("Payment of " + amount + " declined");
        }
    }
}
