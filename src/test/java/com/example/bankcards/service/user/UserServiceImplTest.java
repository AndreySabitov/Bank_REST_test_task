package com.example.bankcards.service.user;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DuplicateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    void checkThrowDuplicateException_IfTryCreateUserAndEmailAlreadyExists() {
        userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("name@mail.ru")
                .build());

        assertThrows(DuplicateException.class, () -> userService.createUser(CreateUserRequest.builder()
                .username("newName")
                .password("password")
                .email("name@mail.ru")
                .build()));
    }

    @Test
    void checkThrowDuplicateException_IfTryCreateUserAndUsernameAlreadyExists() {
        userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("name@mail.ru")
                .build());

        assertThrows(DuplicateException.class, () -> userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("otherName@mail.ru")
                .build()));
    }

    @Test
    void checkCanCreateUser() {
        UserDto user = userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("name@mail.ru")
                .build());

        User createdUser = userRepository.findById(user.getId()).orElseThrow();

        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void checkThrowNotFoundException_whenTryGetUserByUsernameAndUserNotExists() {
        assertThrows(NotFoundException.class, () -> userService.loadUserByUsername("name"));
    }

    @Test
    void checkCanGetUserByUsername() {
        UserDto user = userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("name@mail.ru")
                .build());

        User findedUser = (User) userService.loadUserByUsername("name");

        assertNotNull(findedUser);
        assertEquals(user.getName(), findedUser.getName());
        assertEquals(user.getId(), findedUser.getId());
        assertEquals(user.getEmail(), findedUser.getEmail());
    }

    @Test
    void checkCanDeleteUser() {
        UserDto user = userService.createUser(CreateUserRequest.builder()
                .username("name")
                .password("password")
                .email("name@mail.ru")
                .build());

        userService.deleteUserById(user.getId());

        Optional<User> userOpt = userRepository.findById(user.getId());

        assertTrue(userOpt.isEmpty());
    }
}