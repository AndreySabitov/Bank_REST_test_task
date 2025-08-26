package com.example.bankcards.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обрабатывает все неперехваченные исключения.
     * <p>
     * Используется как обработчик по умолчанию для исключений, не имеющих специализированных обработчиков.
     *
     * @param e перехваченное исключение
     * @return ответ с HTTP-статусом 500 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.error("Unhandled exception", e);
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обрабатывает ситуации отсутствия запрашиваемых ресурсов.
     *
     * @param e исключение типа {@link NotFoundException}
     * @return ответ с HTTP-статусом 404 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обрабатывает ошибки валидации бизнес-логики.
     * <p>
     * Используется для нарушений правил предметной области, обнаруженных в сервисном слое.
     *
     * @param e исключение типа {@link ValidationException}
     * @return ответ с HTTP-статусом 400 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обрабатывает ошибки валидации входных параметров запросов.
     * <p>
     * Автоматически генерируется Spring при нарушении ограничений валидации DTO-объектов.
     *
     * @param e исключение типа {@link MethodArgumentNotValidException}
     * @return ответ с HTTP-статусом 400 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обрабатывает ошибки аутентификации и авторизации.
     * <p>
     * Используется при неудачных попытках входа или отсутствии необходимых прав доступа.
     *
     * @param e исключение типа {@link BadCredentialsException}
     * @return ответ с HTTP-статусом 403 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleBadCredentialsException(final BadCredentialsException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обрабатывает конфликты уникальности данных.
     * <p>
     * Генерируется при попытке создания дублирующих сущностей (например, пользователей с одинаковым email).
     *
     * @param e исключение типа {@link DuplicateException}
     * @return ответ с HTTP-статусом 409 и сообщением об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(final DuplicateException e) {
        return new ErrorResponse(e.getMessage());
    }
}
