package com.example.bankcards.service.user;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DuplicateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        if (userRepository.existsByName(request.getUsername())) {
            throw new DuplicateException("Пользователь с таким именем уже существует");
        }

        var user = User.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        return UserMapper.mapToDto(userRepository.save(user));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    public void deleteUserById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }
    }

    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
