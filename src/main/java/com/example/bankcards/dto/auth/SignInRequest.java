package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO для передачи данных для аутентификации в приложении.
 * Содержит username и password.
 */
@Schema(description = "Запрос на аутентификацию")
@Getter
@AllArgsConstructor
public class SignInRequest {
    @Schema(description = "Имя пользователя", example = "Andy")
    @NotBlank
    private String username;

    @Schema(description = "Пароль", example = "qwerty_best_password")
    @NotBlank
    private String password;
}
