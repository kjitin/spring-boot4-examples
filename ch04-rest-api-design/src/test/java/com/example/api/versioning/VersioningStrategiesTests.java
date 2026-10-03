package com.example.api.versioning;

import com.example.api.product.ProductService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Each slice only loads the controller(s) under test; @Import is needed because the versioning
// package is not component-scanned by the application.
class VersioningStrategiesTests {

    static void assertV1(ResultActions result) throws Exception {
        result.andExpect(status().isOk()).andExpect(jsonPath("$[0].imageUrl").doesNotExist());
    }

    static void assertV2(ResultActions result) throws Exception {
        result.andExpect(status().isOk()).andExpect(jsonPath("$[0].imageUrl").value("/images/1.png"));
    }

    @Nested
    @WebMvcTest(controllers = com.example.api.versioning.uri.ProductController.class)
    @Import({com.example.api.versioning.uri.ProductController.class, ProductService.class})
    class UriPathVariable {
        @Autowired MockMvc mockMvc;

        @Test
        void selectsVersionFromPath() throws Exception {
            assertV1(mockMvc.perform(get("/api/v1/products")));
            assertV2(mockMvc.perform(get("/api/v2/products")));
            mockMvc.perform(get("/api/v3/products")).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @WebMvcTest(controllers = {com.example.api.versioning.separate.ProductV1Controller.class, com.example.api.versioning.separate.ProductV2Controller.class})
    @Import({com.example.api.versioning.separate.ProductV1Controller.class, com.example.api.versioning.separate.ProductV2Controller.class, ProductService.class})
    class SeparateControllers {
        @Autowired MockMvc mockMvc;

        @Test
        void eachVersionHasItsOwnController() throws Exception {
            assertV1(mockMvc.perform(get("/api/v1/products")));
            assertV2(mockMvc.perform(get("/api/v2/products")));
        }
    }

    @Nested
    @WebMvcTest(controllers = com.example.api.versioning.header.ProductController.class)
    @Import({com.example.api.versioning.header.ProductController.class, ProductService.class})
    class CustomHeader {
        @Autowired MockMvc mockMvc;

        @Test
        void selectsVersionFromHeader() throws Exception {
            assertV1(mockMvc.perform(get("/api/products").header("X-API-Version", "1")));
            assertV2(mockMvc.perform(get("/api/products").header("X-API-Version", "2")));
        }
    }

    @Nested
    @WebMvcTest(controllers = com.example.api.versioning.mediatype.ProductController.class)
    @Import({com.example.api.versioning.mediatype.ProductController.class, ProductService.class})
    class ContentNegotiation {
        @Autowired MockMvc mockMvc;

        @Test
        void selectsVersionFromAcceptHeader() throws Exception {
            assertV1(mockMvc.perform(get("/api/products").accept("application/vnd.company.app-v1+json")));
            assertV2(mockMvc.perform(get("/api/products").accept("application/vnd.company.app-v2+json")));
        }
    }

    @Nested
    @WebMvcTest(controllers = com.example.api.versioning.param.ProductController.class)
    @Import({com.example.api.versioning.param.ProductController.class, ProductService.class})
    class QueryParameter {
        @Autowired MockMvc mockMvc;

        @Test
        void selectsVersionFromQueryParam() throws Exception {
            assertV1(mockMvc.perform(get("/api/products").param("version", "1")));
            assertV2(mockMvc.perform(get("/api/products").param("version", "2")));
        }
    }
}
