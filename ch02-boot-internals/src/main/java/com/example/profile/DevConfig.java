package com.example.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;

@Profile("dev")
@Configuration
public class DevConfig {

    @Bean
    public String devMessage() {
        return "Development environment active!";
    }
}
