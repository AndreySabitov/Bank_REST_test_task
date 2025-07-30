package com.example.bankcards.service.user;

import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DuplicateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        if (userRepository.existsByName(user.getName())) {
            throw new DuplicateException("Пользователь с таким именем уже существует");
        }
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
