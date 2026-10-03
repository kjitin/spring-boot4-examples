package com.example.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;

@Profile("prod")
@Configuration
public class ProdConfig {

    @Bean
    public String prodMessage() {
        return "Production environment active!";
    }
}
