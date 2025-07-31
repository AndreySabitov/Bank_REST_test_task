package com.example.bankcards.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserDto {
    private UUID id;
    private String name;
    private String email;
}
