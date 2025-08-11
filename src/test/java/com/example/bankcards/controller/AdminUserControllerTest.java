package com.example.bankcards.controller;

import com.example.bankcards.config.SecurityConfig;
import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.service.auth.JwtTokenService;
import com.example.bankcards.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminUserController.class)
@Import(SecurityConfig.class)
class AdminUserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtTokenService jwtTokenService;

    private final UserDto userDto = UserDto.builder()
            .id(UUID.randomUUID())
            .email("name@mail.ru")
            .name("name")
            .build();

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userDto);

        mvc.perform(post("/admin/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CreateUserRequest.builder()
                                .username("name")
                                .email("name@mail.ru")
                                .password("password11")
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.id", is(userDto.getId().toString())));

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {
        mvc.perform(delete("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto.getId()))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(any(UUID.class));
    }
}