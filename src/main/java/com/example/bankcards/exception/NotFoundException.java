package com.example.bankcards.exception;

/**
 * Исключение при попытке доступа к несуществующему ресурсу.
 * Автоматически обрабатывается глобальным обработчиком исключений.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
