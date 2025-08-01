package com.example.bankcards.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.UUID;

@Getter
@Builder
public class CreateCardDto {
    @NotBlank(message = "Номер карты не может быть пустым")
    @CreditCardNumber
    private String cardNumber;
    @NotNull(message = "Не указан id пользователя")
    private UUID userId;
}
