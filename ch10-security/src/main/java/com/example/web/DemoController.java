package com.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

// Simple endpoints used to demonstrate the URL-based rules of each security configuration
@RestController
public class DemoController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello, anyone!";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal) {
        return "Admin dashboard for " + principal.getName();
    }

    @GetMapping("/api/data")
    public String apiData(Principal principal) {
        return "API data for " + principal.getName();
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        return "Welcome " + principal.getName();
    }
}
