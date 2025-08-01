package com.example.bankcards.service.card.mapper;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.user.User;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class CardMapper {

    public Card mapCreateDtoToCard(User owner, String encryptedCardNumber, String maskedCardNumber) {
        return Card.builder()
                .owner(owner)
                .balance(BigDecimal.valueOf(0.0))
                .encryptedCardNumber(encryptedCardNumber)
                .maskedCardNumber(maskedCardNumber)
                .expirationDate(LocalDate.now().plusYears(10))
                .status(CardStatus.BLOCKED)
                .build();
    }

    public CardDto mapToDto(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .balance(card.getBalance())
                .expirationDate(card.getExpirationDate())
                .maskedCardNumber(card.getMaskedCardNumber())
                .username(card.getOwner().getName())
                .build();
    }
}
