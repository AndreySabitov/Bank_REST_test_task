package com.example.bankcards.dto.auth;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
}
