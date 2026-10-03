package com.example.tx.propagation;

import com.example.tx.domain.Payment;
import com.example.tx.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    @Autowired private PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment processPayment(Payment payment) {
        // This payment will be processed and committed/rolled back independently.
        return paymentRepository.save(payment);
    }
}
