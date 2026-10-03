package com.example.securityvariants;

import com.example.security.CorsConfig;
import com.example.securityvariants.cors.CorsSecurityConfig;
import com.example.securityvariants.multichain.MultipleSecurityChainsConfig;
import com.example.securityvariants.session.SessionFixationSecurityConfig;
import com.example.securityvariants.stateless.StatelessSecurityConfig;
import com.example.web.DemoController;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Each nested class loads exactly one of the alternative SecurityFilterChain configurations.
class SecurityVariantsTests {

    @Nested
    @WebMvcTest(controllers = DemoController.class)
    @Import(MultipleSecurityChainsConfig.class)
    class MultipleChains {
        @Autowired MockMvc mockMvc;

        @Test
        void apiUsesHttpBasicWhileUiUsesFormLogin() throws Exception {
            mockMvc.perform(get("/api/data")).andExpect(status().isUnauthorized())
                    .andExpect(header().exists("WWW-Authenticate"));
            mockMvc.perform(get("/api/data").with(httpBasic("user", "password"))).andExpect(status().isOk());
            mockMvc.perform(get("/home")).andExpect(status().is3xxRedirection());
            mockMvc.perform(get("/public/hello")).andExpect(status().isOk());
        }
    }

    @Nested
    @WebMvcTest(controllers = DemoController.class)
    @Import(StatelessSecurityConfig.class)
    class Stateless {
        @Autowired MockMvc mockMvc;

        @Test
        void noSessionIsCreated() throws Exception {
            MvcResult result = mockMvc.perform(get("/home").with(httpBasic("user", "password")))
                    .andExpect(status().isOk()).andReturn();
            assertThat(result.getRequest().getSession(false)).isNull();
        }
    }

    @Nested
    @WebMvcTest(controllers = DemoController.class)
    @Import({CorsSecurityConfig.class, CorsConfig.class})
    class Cors {
        @Autowired MockMvc mockMvc;

        @Test
        void preflightFromAllowedOriginSucceeds() throws Exception {
            mockMvc.perform(options("/api/data")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                    .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
        }

        @Test
        void preflightFromUnknownOriginIsRejected() throws Exception {
            mockMvc.perform(options("/api/data")
                            .header("Origin", "https://evil.example")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @WebMvcTest(controllers = DemoController.class)
    @Import(SessionFixationSecurityConfig.class)
    class SessionFixation {
        @Autowired MockMvc mockMvc;

        @Test
        void loginMigratesToNewSessionId() throws Exception {
            MockHttpSession session = new MockHttpSession();
            String before = session.getId();
            MvcResult result = mockMvc.perform(formLogin().user("user").password("password")).andReturn();
            assertThat(result.getRequest().getSession(false)).isNotNull();
            assertThat(result.getRequest().getSession(false).getId()).isNotEqualTo(before);
        }
    }
}
