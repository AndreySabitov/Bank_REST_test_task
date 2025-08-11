package com.example.bankcards.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    @NotBlank
    @Size(max = 50)
    private String username;
    @NotBlank
    @Size(max = 255, min = 10)
    private String password;
    @NotBlank
    @Email
    private String email;
}
