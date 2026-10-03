package com.example.scopes;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// Stub the randomly failing fail-fast check so the context always starts
@MockitoBean(types = com.example.failfast.CriticalServiceChecker.class)
@SpringBootTest
@AutoConfigureMockMvc
class ScopeTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ApplicationContext context;

    @Test
    void prototypeBeansAreNewOnEveryLookup() {
        assertThat(context.getBean(PrototypeService.class).getInstanceId())
                .isNotEqualTo(context.getBean(PrototypeService.class).getInstanceId());
        assertThat(context.getBean(SingletonService.class))
                .isSameAs(context.getBean(SingletonService.class));
    }

    @Test
    void requestAndSessionScopes() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String first = mockMvc.perform(get("/scopes").session(session)).andReturn().getResponse().getContentAsString();
        String second = mockMvc.perform(get("/scopes").session(session)).andReturn().getResponse().getContentAsString();
        String otherSession = mockMvc.perform(get("/scopes").session(new MockHttpSession())).andReturn().getResponse().getContentAsString();

        assertThat((String) JsonPath.read(first, "$.singleton")).isEqualTo(JsonPath.read(second, "$.singleton"));
        assertThat((String) JsonPath.read(first, "$.request")).isNotEqualTo(JsonPath.read(second, "$.request"));
        assertThat((String) JsonPath.read(first, "$.session")).isEqualTo(JsonPath.read(second, "$.session"));
        assertThat((String) JsonPath.read(first, "$.session")).isNotEqualTo(JsonPath.read(otherSession, "$.session"));
    }
}
