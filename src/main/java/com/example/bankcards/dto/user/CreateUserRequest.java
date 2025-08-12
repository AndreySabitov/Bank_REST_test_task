package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Dto для создания нового пользователя")
@Getter
@Builder
public class CreateUserRequest {
    @Schema(description = "Имя пользователя", example = "Andy")
    @NotBlank
    @Size(max = 50)
    private String username;
    @Schema(description = "Пароль", example = "qwerty_best_password")
    @NotBlank
    @Size(max = 255, min = 10)
    private String password;
    @Schema(description = "Адрес электронной почты", example = "example@mail.ru")
    @NotBlank
    @Email
    private String email;
}
