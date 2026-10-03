package com.example.hexagonal.application;

import com.example.hexagonal.application.port.UserRepositoryPort;
import com.example.hexagonal.application.port.UserServicePort;
import com.example.hexagonal.domain.User;
import com.example.hexagonal.domain.UserNotFoundException;
import org.springframework.stereotype.Service;

// Application Service (in application layer)
@Service
public class UserService implements UserServicePort {
    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        // Business logic
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }
}
