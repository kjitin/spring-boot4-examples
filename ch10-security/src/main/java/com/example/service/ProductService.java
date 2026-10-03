package com.example.service;

import com.example.security.permission.Product;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @PreAuthorize("hasRole('ADMIN')")
    public String createProduct(String productName) {
        // Only ADMINs can create products
        return "Product '" + productName + "' created by ADMIN.";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<String> getAllProducts() {
        // ADMINs and USERs can view all products
        return List.of("Laptop", "Mouse", "Keyboard");
    }

    @PreAuthorize("hasRole('USER') and #username == authentication.name")
    public String getMyProfile(String username) {
        // A USER can only view their own profile
        return "Profile for " + username;
    }

    @PreAuthorize("hasPermission(#productId, 'Product', 'read')")
    public String getProductDetails(Long productId) {
        // Custom permission check for reading a specific product
        return "Details for product " + productId;
    }

    // Not in the book: exercises the domain-object variant of CustomPermissionEvaluator
    @PreAuthorize("hasPermission(#product, 'edit')")
    public String editProduct(Product product) {
        return "Edited " + product.getName();
    }
}
