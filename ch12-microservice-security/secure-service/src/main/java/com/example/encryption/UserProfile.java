package com.example.encryption;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class UserProfile {
    @Id @GeneratedValue
    private Long id;
    private String username;

    @Convert(converter = SensitiveDataConverter.class)
    private String creditCardNumber;

    protected UserProfile() {}

    public UserProfile(String username, String creditCardNumber) {
        this.username = username;
        this.creditCardNumber = creditCardNumber;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getCreditCardNumber() { return creditCardNumber; }
}
