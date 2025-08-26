package com.example.bankcards.exception;

/**
 * Исключение при ошибке валидации.
 * Автоматически обрабатывается глобальным обработчиком исключений.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
