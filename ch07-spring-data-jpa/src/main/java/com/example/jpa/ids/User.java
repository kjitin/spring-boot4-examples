package com.example.jpa.ids;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") // "user" is a reserved word in most databases (including H2)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // For auto-incrementing Long
    private Long id;
    // ... other fields
    private String username;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
