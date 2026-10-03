package com.example.tx.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class AuditLog {
    @Id @GeneratedValue private Long id;
    private String action;

    protected AuditLog() {}

    public AuditLog(String action) {
        this.action = action;
    }

    public String getAction() { return action; }
}
