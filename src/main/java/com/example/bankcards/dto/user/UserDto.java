package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Schema(description = "Пользователь")
@Getter
@Builder
public class UserDto {
    @Schema(description = "id пользователя", example = "fc5855a2-58d8-4148-9a54-277f3bcb60d8")
    private UUID id;
    @Schema(description = "Имя пользователя", example = "Andy")
    private String name;
    @Schema(description = "Адрес электронной почты пользователя", example = "example@mail.ru")
    private String email;
}
