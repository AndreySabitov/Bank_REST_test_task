package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.UUID;

/**
 * DTO для передачи информации для создания новой карты
 */
@Schema(description = "Dto для передачи данных при создании новой карты")
@Getter
@Builder
public class CreateCardDto {
    @Schema(description = "Номер новой карты", example = "4111111111111111")
    @NotBlank(message = "Номер карты не может быть пустым")
    @CreditCardNumber
    private String cardNumber;
    @Schema(description = "id владельца карты", example = "5545749c-5bb2-40a5-a049-52ecf74b2571")
    @NotNull(message = "Не указан id пользователя")
    private UUID userId;
}
