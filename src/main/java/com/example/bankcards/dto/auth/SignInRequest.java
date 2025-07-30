package com.example.bankcards.dto.auth;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String username;
    private String password;
}
