package com.example.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalEndpointConfiguration {

    private final ProductService productService;

    public FunctionalEndpointConfiguration(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes() {
        return route(GET("/functional/products"),
                request -> ServerResponse.ok().body(productService.findAllProducts(), Product.class))
                .andRoute(GET("/functional/products/{id}"),
                        request -> ServerResponse.ok().body(productService.findProductById(request.pathVariable("id")), Product.class));
    }
}
