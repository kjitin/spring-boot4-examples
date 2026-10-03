package com.example.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OrderServiceUnitTest {

    @Mock // Creates a Mockito mock
    private ProductService productService;

    @InjectMocks // Injects mocks into this instance
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks
    }

    @Test
    void placeOrderShouldReturnTrueWhenStockDeducted() {
        // Given
        String productId = "prod1";
        int quantity = 1;
        when(productService.deductStock(productId, quantity)).thenReturn(true);

        // When
        boolean result = orderService.placeOrder(productId, quantity);

        // Then
        assertThat(result).isTrue();
    }
}
