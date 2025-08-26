package com.example.bankcards.exception;

/**
 * Исключение при попытке добавить данные, которые дублируются и должны быть уникальными.
 * Автоматически обрабатывается глобальным обработчиком исключений.
 */
public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
