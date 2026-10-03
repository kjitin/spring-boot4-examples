package com.example.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceWithMockedDependencyTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean // Replaces the real ProductService with a Mockito mock
    private ProductService productService;

    @Test
    void placeOrderShouldDeductStock() {
        // Given
        String productId = "prod123";
        int quantity = 5;
        when(productService.deductStock(productId, quantity)).thenReturn(true);

        // When
        boolean success = orderService.placeOrder(productId, quantity);

        // Then
        assertThat(success).isTrue();
        verify(productService, times(1)).deductStock(productId, quantity);
    }
}
