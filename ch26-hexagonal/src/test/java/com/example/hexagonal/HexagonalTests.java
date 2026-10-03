package com.example.hexagonal;

import com.example.hexagonal.application.UserService;
import com.example.hexagonal.application.port.UserRepositoryPort;
import com.example.hexagonal.domain.User;
import com.example.hexagonal.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HexagonalTests {

    // The application core can be tested with an in-memory adapter: no Spring, no database
    @Test
    void applicationServiceWorksWithAnyRepositoryAdapter() {
        Map<Long, User> store = new HashMap<>();
        UserRepositoryPort inMemory = new UserRepositoryPort() {
            @Override
            public User save(User user) {
                store.put(1L, user);
                return user;
            }

            @Override
            public Optional<User> findById(Long id) {
                return Optional.ofNullable(store.get(id));
            }
        };
        UserService service = new UserService(inMemory);

        service.createUser(new User("Ada", "ada@example.com"));
        assertThat(service.findUserById(1L).getName()).isEqualTo("Ada");
        assertThatThrownBy(() -> service.findUserById(2L)).isInstanceOf(UserNotFoundException.class);
    }

    @SpringBootTest
    @AutoConfigureMockMvc
    @org.junit.jupiter.api.Nested
    class WiredThroughAdapters {

        @Autowired
        MockMvc mockMvc;

        @Test
        void webAdapterToPersistenceAdapter() throws Exception {
            String id = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Grace\",\"email\":\"grace@example.com\"}"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString().replaceAll(".*\"id\":(\\d+).*", "$1");

            mockMvc.perform(get("/users/" + id)).andExpect(jsonPath("$.email").value("grace@example.com"));
            mockMvc.perform(get("/users/999")).andExpect(status().isNotFound());
        }
    }
}
