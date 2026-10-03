package com.example.api.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(OrderService.class)
class OrderControllerIdempotencyTests {

    @Autowired
    MockMvc mockMvc;

    private static final String ORDER = """
            {"productName":"Laptop","quantity":1}""";

    @Test
    void retryWithSameKeyReturnsOriginalOrder() throws Exception {
        mockMvc.perform(post("/api/orders").header("Idempotency-Key", "abc-123")
                        .contentType(MediaType.APPLICATION_JSON).content(ORDER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(post("/api/orders").header("Idempotency-Key", "abc-123")
                        .contentType(MediaType.APPLICATION_JSON).content(ORDER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(ORDER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }
}
