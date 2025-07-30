package com.example.bankcards.dto.user;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserDto {
    private UUID id;
    private String name;
    private String email;
}
