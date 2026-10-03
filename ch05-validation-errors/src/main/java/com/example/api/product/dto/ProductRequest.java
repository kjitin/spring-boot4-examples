package com.example.api.product.dto;

import com.example.validation.constraints.ValidProductName;
import com.example.validation.groups.OnCreate;
import com.example.validation.groups.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

// Final version from the book: the name rules are expressed with the composed @ValidProductName constraint.
// (The earlier version used @NotBlank + @Size directly with the same groups.)
public record ProductRequest(
    @Null(groups = OnCreate.class, message = "ID must be null for creation")
    @NotNull(groups = OnUpdate.class, message = "ID must not be null for update")
    Long id,

    @ValidProductName(groups = {OnCreate.class, OnUpdate.class}, message = "Product name is invalid")
    String name,

    @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Price must be positive")
    double price,

    String description
) {}
