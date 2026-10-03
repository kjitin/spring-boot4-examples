package com.example.security.permission;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated() || !(permission instanceof String)) {
            return false;
        }

        if (targetDomainObject instanceof Product) {
            Product product = (Product) targetDomainObject;
            String perm = (String) permission;

            // Example: Only the product owner can edit/delete
            if ("edit".equals(perm) || "delete".equals(perm)) {
                return authentication.getName().equals(product.getOwnerUsername());
            }
            // Example: Any authenticated user can read
            if ("read".equals(perm)) {
                return true;
            }
        }
        // Add more domain object types and permission logic here
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // This method is typically used when you only have the ID and type of the domain object
        // You would fetch the domain object here and then delegate to the other hasPermission method
        if (authentication == null || !authentication.isAuthenticated() || !(permission instanceof String)) {
            return false;
        }

        if ("Product".equals(targetType) && targetId instanceof Long) {
            // In a real app, fetch the product from a service/repository
            // For simplicity, let's assume a dummy product owner for demonstration
            String ownerUsername = "user123"; // Replace with actual logic to get owner
            if ("read".equals(permission)) {
                return true; // Any authenticated user can read
            }
            if (("edit".equals(permission) || "delete".equals(permission)) && authentication.getName().equals(ownerUsername)) {
                return true;
            }
        }
        return false;
    }
}
