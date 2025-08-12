package com.example.bankcards.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto с сообщением об ошибке")
public record ErrorResponse(@Schema(description = "Текст ошибки", example = "exception message") String message) {
}
