package com.example.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    @GetMapping("/userinfo")
    public String getUserInfo(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();

            String name = oidcUser.getFullName();
            String email = oidcUser.getEmail();
            String preferredUsername = oidcUser.getPreferredUsername();

            return String.format("Hello, %s! Email: %s, Username: %s", name, email, preferredUsername);
        }
        return "Not an OAuth2 authenticated user.";
    }
}
