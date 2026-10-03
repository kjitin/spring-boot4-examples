package com.example.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Stub the randomly failing fail-fast check so the context always starts
@MockitoBean(types = com.example.failfast.CriticalServiceChecker.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomServiceHealthIndicatorTests {

    @MockitoBean
    MyCriticalBusinessService businessService;

    @Autowired
    CustomServiceHealthIndicator indicator;

    @Autowired
    MockMvc mockMvc;

    @Test
    void reportsUpWhenOperational() throws Exception {
        when(businessService.isOperational()).thenReturn(true);
        assertThat(indicator.health().getStatus()).isEqualTo(Status.UP);
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.customService.status").value("UP"));
    }

    @Test
    void reportsDownWhenNotResponding() throws Exception {
        when(businessService.isOperational()).thenReturn(false);
        assertThat(indicator.health().getDetails()).containsEntry("error", "MyCriticalBusinessService is not responding");
        mockMvc.perform(get("/actuator/health")).andExpect(status().isServiceUnavailable());
    }
}
