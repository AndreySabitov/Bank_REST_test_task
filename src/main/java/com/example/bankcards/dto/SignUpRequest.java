package com.example.bankcards.dto;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
}
