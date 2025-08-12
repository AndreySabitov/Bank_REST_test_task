package com.example.bankcards.controller;

import com.example.bankcards.dto.card.BlockingCardRequestDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.Pageable;
import com.example.bankcards.dto.card.TransferBetweenCardsRequest;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.service.card.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/cards")
@PreAuthorize("hasRole('USER')")
public class UserCardController {
    private final CardService cardService;

    @GetMapping
    public List<CardDto> getAllCardsByUser(@Valid Pageable pageable,
                                           @RequestParam(required = false) CardStatus cardStatus) {
        return cardService.getAllCardsByUser(pageable, cardStatus);
    }

    @PostMapping("/{cardId}/blocking")
    @ResponseStatus(HttpStatus.CREATED)
    public BlockingCardRequestDto addBlockingCardRequest(@PathVariable UUID cardId) {
        return cardService.addBlockingCardRequest(cardId);
    }

    @PostMapping("/transfer")
    public void transferBetweenCards(@Valid @RequestBody TransferBetweenCardsRequest transferRequest) {
        cardService.transferBetweenCards(transferRequest);
    }

    @GetMapping("/{cardId}/balance")
    public BigDecimal getCardBalance(@PathVariable UUID cardId) {
        return cardService.getBalance(cardId);
    }
}
