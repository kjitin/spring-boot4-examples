package com.example.hexagonal.infrastructure.persistence;

import com.example.hexagonal.application.port.UserRepositoryPort;
import com.example.hexagonal.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Driven Adapter (in infrastructure.persistence)
@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {
    private final JpaUserRepository jpaUserRepository; // Spring Data JPA repository

    public JpaUserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }
}
