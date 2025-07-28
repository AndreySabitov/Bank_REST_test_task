package com.example.bankcards.repository;

import com.example.bankcards.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
   Optional<User> findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}