package com.example;

import com.example.config.AppDataSourceProperties;
import com.example.config.AppDataSourceRecordProperties;
import com.example.env.MyEnvironmentReader;
import com.example.myservice.MyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class Ch02ApplicationTests {

    @Autowired
    MyService myService;

    @Autowired
    AppDataSourceProperties properties;

    @Autowired
    AppDataSourceRecordProperties recordProperties;

    @Autowired
    MyEnvironmentReader environmentReader;

    @Autowired
    String devMessage;

    @Autowired
    MockMvc mockMvc;

    @Test
    void autoConfigurationContributesMyService() {
        assertThat(myService.greet()).isEqualTo("Hello from MyService");
    }

    @Test
    void configurationPropertiesAreBound() {
        assertThat(properties.getUrl()).isEqualTo("jdbc:h2:mem:demo");
        assertThat(recordProperties.username()).isEqualTo("sa");
    }

    @Test
    void devProfileIsActive() {
        assertThat(devMessage).isEqualTo("Development environment active!");
        assertThat(environmentReader.getProperty("app.greeting")).isEqualTo("Hello from the dev profile");
        environmentReader.printActiveProfiles();
    }

    @Test
    void actuatorEndpointsAreExposed() throws Exception {
        mockMvc.perform(get("/actuator/health")).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
        mockMvc.perform(get("/actuator/beans")).andExpect(status().isOk());
        mockMvc.perform(get("/actuator/env")).andExpect(status().isOk());
    }
}
