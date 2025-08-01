package com.example.bankcards.service.user.mapper;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class UserMapper {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public User mapCreateDtoToUser(CreateUserRequest request) {
        return User.builder()
                .role(Role.ROLE_USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getUsername())
                .build();
    }
}
