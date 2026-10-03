package com.example.serviceclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductServiceClient {

    private final WebClient webClient;
    private final String productServiceUrl;

    // The base URL is configurable (default http://product-service as in the book) so the client can be tested
    public ProductServiceClient(WebClient webClient,
                                @Value("${product-service.url:http://product-service}") String productServiceUrl) {
        this.webClient = webClient;
        this.productServiceUrl = productServiceUrl;
    }

    public String getProducts() {
        return webClient.get()
                .uri(productServiceUrl + "/api/products")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
