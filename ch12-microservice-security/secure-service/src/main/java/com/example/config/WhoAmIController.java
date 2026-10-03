package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WhoAmIController {

    @Value("${spring.datasource.username}")
    private String datasourceUser;

    @GetMapping("/whoami")
    public String whoAmI(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/datasource-user")
    public String datasourceUser() {
        return datasourceUser;
    }
}
