package com.example.security;

import com.example.security.permission.Product;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MethodSecurityTests {

    @Autowired
    ProductService productService;

    @Test
    @WithMockUser(roles = "USER")
    void usersCannotCreateProducts() {
        assertThatThrownBy(() -> productService.createProduct("Tablet")).isInstanceOf(AuthorizationDeniedException.class);
        assertThat(productService.getAllProducts()).contains("Laptop");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminsCanCreateProducts() {
        assertThat(productService.createProduct("Tablet")).isEqualTo("Product 'Tablet' created by ADMIN.");
    }

    @Test
    @WithMockUser(username = "alice", roles = "USER")
    void usersCanOnlySeeTheirOwnProfile() {
        assertThat(productService.getMyProfile("alice")).isEqualTo("Profile for alice");
        assertThatThrownBy(() -> productService.getMyProfile("bob")).isInstanceOf(AuthorizationDeniedException.class);
    }

    @Test
    @WithMockUser(username = "user123")
    void customPermissionEvaluator() {
        assertThat(productService.getProductDetails(42L)).isEqualTo("Details for product 42");
        assertThat(productService.editProduct(new Product(1L, "Lamp", "user123"))).isEqualTo("Edited Lamp");
        assertThatThrownBy(() -> productService.editProduct(new Product(2L, "Desk", "someone-else")))
                .isInstanceOf(AuthorizationDeniedException.class);
    }
}
