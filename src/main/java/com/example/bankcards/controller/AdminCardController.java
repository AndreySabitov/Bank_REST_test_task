package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
import com.example.bankcards.dto.card.Pageable;
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

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления картами администратором.
 * Обрабатывает HTTP-запросы, связанные с действиями администратора с картами пользователей.
 */
@Tag(name = "ADMIN: Управление картами")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@ApiResponses({
        @ApiResponse(responseCode = "403", description = "Отказано в доступе",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
}
)
public class AdminCardController {
    private final CardService cardService;

    /**
     * Создание новой карты для пользователя администратором
     *
     * @param createCardDto информация необходимая для создания новой карты ({@link CreateCardDto})
     * @return {@link CardDto} с данными о созданной карте
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание новой карты")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "Ошибка составления запроса",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Карта с таким номером уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CardDto createCard(@Valid @RequestBody CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }

    /**
     * Блокировка карты по ID
     *
     * @param cardId идентификатор карты для блокировки ({@link UUID})
     */
    @PatchMapping("/{cardId}/blocking")
    @Operation(summary = "Блокировка карты")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void blockingCard(@PathVariable @Parameter(description = "id блокируемой карты") UUID cardId) {
        cardService.blockingCard(cardId);
    }

    /**
     * Блокировка карты по запросу пользователя
     *
     * @param requestId идентификатор запроса на блокировку карты ({@link UUID})
     */
    @PatchMapping("/{requestId}/blocking/byRequest")
    @Operation(summary = "Блокировка карты по запросу пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Запрос на блокировку карты не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void blockingCardByRequest(@PathVariable @Parameter(description = "id запроса на блокировку") UUID requestId) {
        cardService.blockingCardByRequest(requestId);
    }

    /**
     * Активация карты по ID
     *
     * @param cardId идентификатор карты, которую необходимо активировать ({@link UUID})
     */
    @PatchMapping("/{cardId}/activate")
    @Operation(summary = "Активация карты")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Истек срок действия карты",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void activateCard(@PathVariable @Parameter(description = "id активируемой карты") UUID cardId) {
        cardService.activateCard(cardId);
    }

    /**
     * Удаление карты по ID
     *
     * @param cardId идентификатор карты, которую нужно удалить ({@link UUID})
     */
    @DeleteMapping("/{cardId}")
    @Operation(summary = "Удаление карты по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void deleteCard(@PathVariable @Parameter(description = "id удаляемой карты") UUID cardId) {
        cardService.deleteCard(cardId);
    }

    /**
     * Получение информации о карте по ID
     *
     * @param cardId идентификатор карты ({@link UUID})
     * @return {@link CardDto} с данными карты
     */
    @GetMapping("/{cardId}")
    @Operation(summary = "Получение информацию о карте по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public CardDto getCardById(@PathVariable
                               @Parameter(description = "id карты, по которой нужно получить информацию") UUID cardId) {
        return cardService.getCardById(cardId);
    }

    /**
     * Получение информации о всех картах
     *
     * @return Список {@link CardDto} с данными о всех картах
     */
    @GetMapping
    @Operation(summary = "Просмотр всех карт")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public List<CardDto> getAllCards(@Valid Pageable pageable) {
        return cardService.getAllCards(pageable);
    }
}
