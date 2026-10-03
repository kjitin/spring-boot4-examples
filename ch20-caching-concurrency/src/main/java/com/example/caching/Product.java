package com.example.caching;

import java.io.Serializable;

// Serializable added so the same class also works with the Redis cache (JDK serialization by default)
public class Product implements Serializable {
    private Long id;
    private String name;
    private double price;

    public Product(Long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Product{" +
               "id=" + id +
               ", name='" + name + "'" +
               ", price=" + price +
               '}';
    }
}
