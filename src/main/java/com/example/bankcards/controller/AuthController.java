package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.SignInRequest;
import com.example.bankcards.dto.auth.TokenResponse;
import com.example.bankcards.exception.ErrorResponse;
import com.example.bankcards.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для аутентификации.
 */
@Tag(name = "Аутентификация")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Аутентификация пользователя
     *
     * @param signInRequest данные для аутентификации пользователя ({@link SignInRequest})
     * @return {@link TokenResponse} с JWT - токеном
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Не задано имя пользователя или пароль",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Отказано в доступе",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public TokenResponse signIn(@RequestBody @Valid SignInRequest signInRequest) {
        return authenticationService.signIn(signInRequest);
    }
}
