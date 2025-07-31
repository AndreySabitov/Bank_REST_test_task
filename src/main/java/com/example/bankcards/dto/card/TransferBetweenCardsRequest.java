package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TransferBetweenCardsRequest {
    @NotNull
    private UUID fromCard;
    @NotNull
    private UUID toCard;
    @NotNull
    @Positive
    private BigDecimal amount;
}
