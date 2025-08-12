package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.entity.card.CardStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CardService {
    CardDto createCard(CreateCardDto createCardDto);

    void blockingCard(UUID cardId);

    void blockingCardByRequest(UUID requestId);

    void activateCard(UUID cardId);

    void deleteCard(UUID cardID);

    CardDto getCardById(UUID cardId);

    List<CardDto> getAllCards(Pageable pageable);

    List<CardDto> getAllCardsByUser(Pageable pageable, CardStatus cardStatus);

    BlockingCardRequestDto addBlockingCardRequest(UUID cardId);

    void transferBetweenCards(TransferBetweenCardsRequest transferRequest);

    BigDecimal getBalance(UUID cardId);
}
