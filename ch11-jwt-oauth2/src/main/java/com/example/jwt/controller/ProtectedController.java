package com.example.jwt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/protected")
    public String protectedEndpoint(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();
        String roles = jwt.getClaimAsString("roles"); // Assuming roles are in a 'roles' claim
        return "Hello, " + username + "! Your roles: " + roles;
    }

    @GetMapping("/public/info")
    public String publicInfo() {
        return "public";
    }
}
