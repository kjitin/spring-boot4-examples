package com.example.jpa.ids;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

// Identifier example. Entity/table names are customised so this class can coexist with
// com.example.jpa.domain.Product in the same persistence unit.
@Entity(name = "UuidProduct")
@Table(name = "uuid_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // For UUID generation
    private UUID id;
    // ... other fields
    private String name;

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
