package com.example.api.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class ProductControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void listsWithPaginationFilteringAndSorting() throws Exception {
        mockMvc.perform(get("/api/v1/products").param("sort", "price,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
        mockMvc.perform(get("/api/v1/products").param("category", "furniture"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Desk"));
        mockMvc.perform(get("/api/v1/products").param("page", "1").param("size", "2"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void crudLifecycle() throws Exception {
        String body = """
                {"name":"Monitor","price":199.0,"description":"27 inch"}""";
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
        mockMvc.perform(get("/api/v1/products/4")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Monitor"));
        mockMvc.perform(put("/api/v1/products/4").contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("199.0", "149.0")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(149.0));
        mockMvc.perform(delete("/api/v1/products/4")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/products/4")).andExpect(status().isNotFound());
    }
}
