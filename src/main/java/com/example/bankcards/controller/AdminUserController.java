package com.example.bankcards.controller;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.exception.ErrorResponse;
import com.example.bankcards.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Контроллер для управления пользователями администратором.
 * Обрабатывает HTTP-запросы, связанные с управлением пользователями.
 */
@RequestMapping("/v1/admin/users")
@Tag(name = "ADMIN: Управление пользователями")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Отказано в доступе",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public class AdminUserController {
    private final UserService userService;

    /**
     * Создание нового пользователя
     *
     * @param createUserRequest содержит данные для создания нового пользователя
     * @return UserDto с данными пользователя
     * @see CreateUserRequest
     * @see UserDto
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Имя пользователя и email должны быть уникальными",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserDto createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    /**
     * Удаление пользователя по ID
     *
     * @param userId идентификатор пользователя (UUID)
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Удаление пользователя по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void deleteUser(@PathVariable @Parameter(description = "id удаляемого пользователя") UUID userId) {
        userService.deleteUserById(userId);
    }
}
