package com.example.hexagonal.application.port;

import com.example.hexagonal.domain.User;

// Driving Port (in application layer)
public interface UserServicePort {
    User createUser(User user);
    User findUserById(Long id);
}
