package com.example.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtResourceServerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void rejectsRequestsWithoutToken() throws Exception {
        mockMvc.perform(get("/protected")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/public/info")).andExpect(status().isOk());
    }

    @Test
    void readsClaimsFromJwt() throws Exception {
        mockMvc.perform(get("/protected").with(jwt().jwt(j -> j.subject("alice").claim("roles", "admin"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, alice! Your roles: admin"));
    }
}
