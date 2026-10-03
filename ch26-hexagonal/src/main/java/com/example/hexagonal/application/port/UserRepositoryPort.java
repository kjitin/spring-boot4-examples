package com.example.hexagonal.application.port;

import com.example.hexagonal.domain.User;

import java.util.Optional;

// Driven Port (in application or domain layer)
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
}
