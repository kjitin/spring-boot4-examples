package com.example.jwt;

import com.example.jwt.controller.ProtectedController;
import com.example.jwt.internal.InternalJwtResourceServerConfig;
import com.example.jwt.internal.JwtGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// End-to-end: a token signed by JwtGenerator (JJWT) is validated by Spring Security's NimbusJwtDecoder
@WebMvcTest(controllers = ProtectedController.class)
@Import(InternalJwtResourceServerConfig.class)
class InternalJwtTests {

    @Autowired MockMvc mockMvc;
    @Autowired JwtGenerator jwtGenerator;

    @Test
    void acceptsTokenSignedWithSharedSecret() throws Exception {
        String token = jwtGenerator.generateToken("order-service", "SERVICE");
        mockMvc.perform(get("/protected").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, order-service! Your roles: SERVICE"));
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() throws Exception {
        String foreignToken = new JwtGenerator().generateToken("intruder", "ADMIN");
        mockMvc.perform(get("/protected").header("Authorization", "Bearer " + foreignToken))
                .andExpect(status().isUnauthorized());
    }
}
