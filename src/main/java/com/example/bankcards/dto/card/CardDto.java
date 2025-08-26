package com.example.bankcards.dto.card;

import com.example.bankcards.entity.card.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для передачи информации о карте
 */
@Schema(description = "Dto для отображения информации о карте")
@Getter
@Builder
public class CardDto {
    @Schema(description = "id карты", example = "fc5855a2-58d8-4148-9a54-277f3bcb60d8")
    private UUID id;
    @Schema(description = "Замаскированный номер карты", example = "**** **** **** 1111")
    private String maskedCardNumber;
    @Schema(description = "Дата до которой действительно карта", example = "2035-08-11")
    private LocalDate expirationDate;
    @Schema(description = "Баланс карты", example = "0.0")
    private BigDecimal balance;
    @Schema(description = "Имя хозяйна карты", example = "Andy")
    private String username;
    @Schema(description = "Статус карты")
    private CardStatus status;
}
