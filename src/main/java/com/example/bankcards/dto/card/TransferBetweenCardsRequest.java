package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dto для запроса перевода средств с карты на карту")
@Getter
@AllArgsConstructor
public class TransferBetweenCardsRequest {
    @Schema(description = "id карты откуда нужно совершить перевод", example = "5545749c-5bb2-40a5-a049-52ecf74b2571")
    @NotNull
    private UUID fromCard;
    @Schema(description = "id карты на которую нужно совершить перевод", example = "5545749c-5bb2-40a5-a049-52ecf74b2571")
    @NotNull
    private UUID toCard;
    @Schema(description = "Сумма перевода", example = "100.0")
    @NotNull
    @Positive
    private BigDecimal amount;
}
