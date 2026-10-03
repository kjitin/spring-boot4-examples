package com.example.tx.hexagonal.domain;

// Pure domain object: no JPA annotations
public class Product {
    private final Long id;
    private final String name;
    private final double price;
    private int stock;

    public Product(Long id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void deductStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalStateException("Insufficient stock for product " + name);
        }
        stock -= quantity;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}
