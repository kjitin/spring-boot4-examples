package com.example.tx.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Payment {
    @Id @GeneratedValue private Long id;
    private double amount;

    protected Payment() {}

    public Payment(double amount) {
        this.amount = amount;
    }

    public Long getId() { return id; }
    public double getAmount() { return amount; }
}
