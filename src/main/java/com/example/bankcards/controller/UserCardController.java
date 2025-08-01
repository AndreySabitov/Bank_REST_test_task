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
@RequestMapping("/users/cards")
@PreAuthorize("hasRole('USER')")
public class UserCardController {
    private final CardService cardService;

    @GetMapping
    public List<CardDto> getAllCardsByUser(@RequestBody UUID userID, @Valid Pageable pageable,
                                           @RequestParam(required = false) CardStatus cardStatus) {
        return cardService.getAllCardsByUser(userID, pageable, cardStatus);
    }

    @PostMapping("/blocking")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BlockingCardRequestDto addBlockingCardRequest(@RequestBody UUID cardId) {
        return cardService.addBlockingCardRequest(cardId);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void transferBetweenCards(@Valid @RequestBody TransferBetweenCardsRequest transferRequest) {
        cardService.transferBetweenCards(transferRequest);
    }

    @GetMapping("/balance")
    public BigDecimal getCardBalance(@RequestBody UUID cardId) {
        return cardService.getBalance(cardId);
    }
}
