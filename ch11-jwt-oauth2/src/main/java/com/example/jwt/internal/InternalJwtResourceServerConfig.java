package com.example.jwt.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class InternalJwtResourceServerConfig {

    // Exposed as a bean (instead of "new JwtGenerator()" inside this class, as in the book) so that the
    // token issuer and the decoder share the same randomly generated key. In production the key should be
    // securely managed, e.g., loaded from environment variables or a secret store.
    @Bean
    public JwtGenerator jwtGenerator() {
        return new JwtGenerator();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtGenerator jwtGenerator) {
        SecretKey jwtSecretKey = jwtGenerator.getSecretKey();
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
    }
}
