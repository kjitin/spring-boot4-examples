package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Principal;

@Configuration
public class RateLimiterConfig {

    @Bean
    KeyResolver userKeyResolver() {
        // Rate limit based on authenticated user principal name.
        // (The book wraps this in Mono.just(... .block()); blocking is not allowed on the
        // gateway's Netty event loop, so the reactive chain is returned directly.)
        return exchange -> exchange.getPrincipal().map(Principal::getName).defaultIfEmpty("anonymous");
    }
}
