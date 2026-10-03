package com.example.security.permission;

// Dummy Product class for demonstration
public class Product {
    private Long id;
    private String name;
    private String ownerUsername;

    public Product(Long id, String name, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.ownerUsername = ownerUsername;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOwnerUsername() { return ownerUsername; }
}
