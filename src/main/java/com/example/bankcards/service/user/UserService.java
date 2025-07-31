package com.example.bankcards.service.user;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserRequest request);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    void deleteUserById(UUID userId);

    User getCurrentUser();
}
