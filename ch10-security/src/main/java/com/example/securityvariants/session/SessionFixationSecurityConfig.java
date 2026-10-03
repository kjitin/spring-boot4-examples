package com.example.securityvariants.session;

import com.example.securityvariants.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SessionFixationSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionFixation(fixation -> fixation.migrateSession()) // Default: creates new session, copies attributes
                // .sessionFixation(fixation -> fixation.newSession()) // Creates an entirely new session, discards old attributes
                // .sessionFixation(fixation -> fixation.none()) // No session fixation protection (use with caution)
            )
            // ... other security configurations
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .formLogin(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return Users.inMemoryUsers();
    }
}
