package com.example.bankcards.controller;

import com.example.bankcards.config.SecurityConfig;
import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.TokenResponse;
import com.example.bankcards.service.auth.AuthenticationService;
import com.example.bankcards.service.auth.JwtTokenService;
import com.example.bankcards.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private AuthenticationService authenticationService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtTokenService jwtTokenService;

    private final TokenResponse tokenResponse = new TokenResponse("newJwtToken");

    @Test
    void signIn() throws Exception {
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(tokenResponse);

        mvc.perform(post("/auth/sign-in")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignInRequest("username",
                                "password11")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(tokenResponse.getToken())));

        verify(authenticationService, times(1)).signIn(any(SignInRequest.class));
    }
}