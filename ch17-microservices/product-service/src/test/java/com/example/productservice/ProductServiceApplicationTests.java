package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Starts without Eureka or a Config Server: the config import is optional and registration is disabled
@SpringBootTest(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void servesProductsAndFallsBackToLocalConfig() throws Exception {
        mockMvc.perform(get("/products")).andExpect(jsonPath("$[0]").value("Laptop"));
        mockMvc.perform(get("/")).andExpect(jsonPath("$.length()").value(3));
        mockMvc.perform(get("/greeting")).andExpect(content().string("Hello from local configuration"));
    }
}
