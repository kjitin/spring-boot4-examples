package com.example.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void publicEndpointsArePermitted() throws Exception {
        mockMvc.perform(get("/public/hello")).andExpect(status().isOk());
    }

    @Test
    void anonymousUsersAreChallenged() throws Exception {
        // Browsers are redirected to the login form, other clients get a 401 Basic challenge
        mockMvc.perform(get("/home").accept(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/home")).andExpect(status().isUnauthorized());
    }

    @Test
    void adminAreaRequiresAdminRole() throws Exception {
        mockMvc.perform(get("/admin/dashboard").with(httpBasic("user", "password"))).andExpect(status().isForbidden());
        mockMvc.perform(get("/admin/dashboard").with(httpBasic("admin", "adminpass")))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin dashboard for admin"));
    }

    @Test
    void formLoginWorks() throws Exception {
        mockMvc.perform(formLogin().user("user").password("password")).andExpect(authenticated().withUsername("user"));
    }

    @Test
    void methodSecurityAndPermissionEvaluatorThroughTheWebLayer() throws Exception {
        mockMvc.perform(get("/api/products").with(httpBasic("user", "password"))).andExpect(status().isOk());
        mockMvc.perform(get("/api/products/1").with(httpBasic("user", "password")))
                .andExpect(content().string("Details for product 1"));
        mockMvc.perform(post("/api/products").content("Tablet").with(csrf()).with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/api/products").content("Tablet").with(csrf()).with(httpBasic("admin", "adminpass")))
                .andExpect(status().isOk());
    }
}
