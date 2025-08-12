package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Ответ с токеном доступа")
@Getter
@AllArgsConstructor
public class TokenResponse {
    @Schema(description = "Токен доступа")
    private String token;
}
