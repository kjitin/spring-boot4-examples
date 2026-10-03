package com.example.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProblemDetailsTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void validRequestIsCreated() throws Exception {
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":999.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void invalidRequestReturnsProblemDetail() throws Exception {
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"L\",\"price\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Invalid Request Body"))
                .andExpect(jsonPath("$.type").value("https://example.com/problems/invalid-request-body"))
                .andExpect(jsonPath("$.errors.name").value("Product name must be between 2 and 100 characters"))
                .andExpect(jsonPath("$.errors.price").value("Price must be positive"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void missingResourceReturns404ProblemDetail() throws Exception {
        mockMvc.perform(get("/api/v1/products/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Product with id 42 not found"));
    }

    @Test
    void unexpectedErrorsAreNotLeaked() throws Exception {
        mockMvc.perform(get("/api/v1/products/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."));
    }

    @Test
    void validationGroupsApplyDifferentRulesForCreateAndUpdate() throws Exception {
        // id must be null on create
        mockMvc.perform(post("/api/v2/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":5,\"name\":\"Desk\",\"price\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.id").value("ID must be null for creation"));
        mockMvc.perform(post("/api/v2/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Desk\",\"price\":10}"))
                .andExpect(status().isCreated());
        // id must be present on update
        mockMvc.perform(put("/api/v2/products/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Desk\",\"price\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.id").value("ID must not be null for update"));
        // composed @ValidProductName constraint
        mockMvc.perform(post("/api/v2/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \",\"price\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }
}
