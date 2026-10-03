package com.example.securityvariants.multichain;

import com.example.securityvariants.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MultipleSecurityChainsConfig {

    @Bean
    @Order(1) // Required: the more specific chain must be consulted before the catch-all chain below
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // This filter chain only applies to /api/**
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults()) // API uses HTTP Basic
            .csrf(csrf -> csrf.disable()); // Disable CSRF for stateless APIs
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain formLoginSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults()); // Web UI uses form login
        return http.build();
    }

    // ... UserDetailsService bean as before
    @Bean
    public UserDetailsService userDetailsService() {
        return Users.inMemoryUsers();
    }
}
