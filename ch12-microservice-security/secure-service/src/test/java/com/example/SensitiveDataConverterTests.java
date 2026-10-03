package com.example;

import com.example.encryption.UserProfile;
import com.example.encryption.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SensitiveDataConverterTests {

    @Autowired UserProfileRepository repository;
    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    void creditCardNumberIsEncryptedAtRest() {
        UserProfile saved = repository.save(new UserProfile("alice", "4111111111111111"));

        String stored = jdbcTemplate.queryForObject(
                "SELECT credit_card_number FROM user_profile WHERE id = ?", String.class, saved.getId());
        assertThat(stored).isNotEqualTo("4111111111111111").doesNotContain("4111");

        assertThat(repository.findById(saved.getId()).orElseThrow().getCreditCardNumber()).isEqualTo("4111111111111111");
    }
}
