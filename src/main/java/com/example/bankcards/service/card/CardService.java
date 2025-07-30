package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
import com.example.bankcards.dto.card.Pageable;

import java.util.List;
import java.util.UUID;

public interface CardService {
    CardDto createCard(CreateCardDto createCardDto);

    boolean blockingCard(UUID cardId);

    boolean activateCard(UUID cardId);

    void deleteCard(UUID cardID);

    CardDto getCardById(UUID cardId);

    List<CardDto> getAllCards(Pageable pageable);
}
