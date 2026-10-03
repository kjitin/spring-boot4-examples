package com.example.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JpaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Provides methods for interacting with the persistence context

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenFindByName_thenReturnProduct() {
        // given
        Product apple = new Product("Apple", 1.0);
        entityManager.persist(apple);
        entityManager.flush();

        // when
        Product found = productRepository.findByName(apple.getName()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(apple.getName());
    }
}
