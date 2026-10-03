package com.example.hexagonal.infrastructure.persistence;

import com.example.hexagonal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA repository used only inside the persistence adapter
public interface JpaUserRepository extends JpaRepository<User, Long> {
}
