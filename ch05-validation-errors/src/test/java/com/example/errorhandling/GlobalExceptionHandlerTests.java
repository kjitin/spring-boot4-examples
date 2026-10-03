package com.example.errorhandling;

import com.example.api.product.ProductController;
import com.example.api.product.ProductService;
import com.example.legacy.errorhandling.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Exercises the map-based handler on its own (the Problem Details handler is filtered out of the slice)
@WebMvcTest(controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomResponseEntityExceptionHandler.class))
@Import({GlobalExceptionHandler.class, ProductService.class})
class GlobalExceptionHandlerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void validationErrorsAreReturnedAsMap() throws Exception {
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"price\":5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void notFoundIsHandled() throws Exception {
        mockMvc.perform(get("/api/v1/products/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with id 7 not found"));
    }

    @Test
    void genericErrorsAreHandled() throws Exception {
        mockMvc.perform(get("/api/v1/products/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
