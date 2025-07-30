package com.example.bankcards.dto.card;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class CardDto {
    private UUID id;
    private String maskedCardNumber;
    private LocalDate expirationDate;
    private BigDecimal balance;
    private String username;
}
