package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class SecurityAuditListenerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void logsSuccessfulAndFailedLogins(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/whoami").with(httpBasic("user", "password"))).andExpect(status().isOk());
        mockMvc.perform(get("/whoami").with(httpBasic("user", "wrong"))).andExpect(status().isUnauthorized());

        assertThat(output).contains("AUDIT: User 'user' successfully authenticated");
        assertThat(output).contains("AUDIT: Failed authentication attempt for user 'user'");
    }
}
