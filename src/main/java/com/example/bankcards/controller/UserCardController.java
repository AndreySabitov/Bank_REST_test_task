package com.example.bankcards.controller;

import com.example.bankcards.dto.card.BlockingCardRequestDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.Pageable;
import com.example.bankcards.dto.card.TransferBetweenCardsRequest;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.exception.ErrorResponse;
import com.example.bankcards.service.card.CardService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления картами пользователем.
 * Обрабатывает HTTP-запросы, связанные с действиями пользователей с картами.
 */
@RequestMapping("/v1/user/cards")
@Tag(name = "USER: Действия с картами")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Отказано в доступе",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public class UserCardController {
    private final CardService cardService;

    /**
     * Получение пользователем информации о всех своих картах
     *
     * @param pageable для пагинации ({@link Pageable})
     * @param cardStatus для фильтрации по статусу ({@link CardStatus})
     * @return список {@link CardDto} с информацией о картах
     */
    @GetMapping
    @Operation(summary = "Просмотр всех карт текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CardDto> getAllCardsByUser(@Valid Pageable pageable,
                                           @RequestParam(required = false) CardStatus cardStatus) {
        return cardService.getAllCardsByUser(pageable, cardStatus);
    }

    /**
     * Создание запроса на блокировку карты
     *
     * @param cardId идентификатор карты, которую нужно заблокировать ({@link UUID})
     * @return {@link BlockingCardRequestDto} с данными о созданном запросе на блокировку карты
     */
    @PostMapping("/{cardId}/blocking")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Запрос блокировки карты текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "404", description = "Пользователь или карта не найдены",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public BlockingCardRequestDto addBlockingCardRequest(
            @PathVariable @Parameter(description = "id карты, блокировку которой нужно запросить") UUID cardId) {
        return cardService.addBlockingCardRequest(cardId);
    }

    /**
     * Перевод денежных средств между картами пользователя
     *
     * @param transferRequest информация об исходной и целевой карте, а так же сумме перевода. ({@link TransferBetweenCardsRequest})
     */
    @PostMapping("/transfer")
    @Operation(summary = "Перевод денежных средств между картами текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден пользователь или карты",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void transferBetweenCards(@Valid @RequestBody TransferBetweenCardsRequest transferRequest) {
        cardService.transferBetweenCards(transferRequest);
    }

    /**
     * Просмотреть баланс карты
     *
     * @param cardId идентификатор карты пользователя ({@link UUID})
     * @return баланс карты
     */
    @GetMapping("/{cardId}/balance")
    @Operation(summary = "Просмотр баланса карты текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден пользователь или карты",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public BigDecimal getCardBalance(
            @PathVariable @Parameter(description = "id карты, баланс которой нужно просмотреть") UUID cardId) {
        return cardService.getBalance(cardId);
    }
}
