package com.example.function;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cloud.function.adapter.aws.FunctionInvoker;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FunctionTests {

    @Autowired MockMvc mockMvc;
    @Autowired FunctionCatalog catalog;

    String invoke(String path, MediaType type, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(path).contentType(type).content(body)).andReturn();
        if (result.getRequest().isAsyncStarted()) {
            result = mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk()).andReturn();
        }
        return result.getResponse().getContentAsString();
    }

    @Test
    void functionsAreExposedAsHttpEndpoints() throws Exception {
        // curl -H "Content-Type: text/plain" -X POST -d "hello" http://localhost:8080/uppercase
        assertThat(invoke("/uppercase", MediaType.TEXT_PLAIN, "hello")).isEqualTo("HELLO");
        // curl -H "Content-Type: application/json" -X POST -d '{"name":"John","city":"New York"}' http://localhost:8080/greetUser
        assertThat(invoke("/greetUser", MediaType.APPLICATION_JSON, "{\"name\":\"John\",\"city\":\"New York\"}"))
                .isEqualTo("Hello, John from New York!");
    }

    @Test
    void composedFunctionFromCatalog() {
        Function<FunctionConfiguration.User, String> composed = catalog.lookup("greetUser|uppercase");
        FunctionConfiguration.User user = new FunctionConfiguration.User();
        user.setName("Ada");
        user.setCity("London");
        assertThat(composed.apply(user)).isEqualTo("HELLO, ADA FROM LONDON!");
    }

    @Test
    void awsLambdaHandlerInvokesDefaultFunction() throws Exception {
        // FunctionInvoker is the handler configured in AWS Lambda
        // (org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest)
        System.setProperty("MAIN_CLASS", FunctionApplication.class.getName());
        FunctionInvoker invoker = new FunctionInvoker();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        invoker.handleRequest(new ByteArrayInputStream("{\"name\":\"Grace\",\"city\":\"Arlington\"}".getBytes(StandardCharsets.UTF_8)),
                out, mock(Context.class));
        assertThat(out.toString(StandardCharsets.UTF_8)).contains("HELLO, GRACE FROM ARLINGTON!");
    }
}
