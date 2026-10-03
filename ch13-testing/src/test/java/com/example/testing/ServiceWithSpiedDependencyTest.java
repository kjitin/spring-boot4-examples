package com.example.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ServiceWithSpiedDependencyTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean // Creates a spy of the real ProductService (@SpyBean was removed in Spring Boot 4)
    private ProductService productService;

    @Test
    void placeOrderShouldCallRealDeductStockAndCanBeStubbed() {
        // Given
        String productId = "prod456";
        int quantity = 2;

        // When using a spy, real methods are called by default
        orderService.placeOrder(productId, quantity);

        // Then verify the real method was called
        verify(productService, times(1)).deductStock(productId, quantity);

        // You can also stub specific methods on a spy
        doReturn(false).when(productService).deductStock("prod789", 1);
        boolean success = orderService.placeOrder("prod789", 1);
        assertThat(success).isFalse();
        verify(productService, times(1)).deductStock("prod789", 1);
    }
}
