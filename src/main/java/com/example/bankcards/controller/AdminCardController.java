package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
import com.example.bankcards.dto.card.Pageable;
import com.example.bankcards.service.card.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
    private final CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto createCard(@Valid @RequestBody CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }

    @PatchMapping("/{cardId}/blocking")
    public void blockingCard(@PathVariable UUID cardId) {
        cardService.blockingCard(cardId);
    }

    @PatchMapping("/{requestId}/blocking/byRequest")
    public void blockingCardByRequest(@PathVariable UUID requestId) {
        cardService.blockingCardByRequest(requestId);
    }

    @PatchMapping("/{cardId}/activate")
    public void activateCard(@PathVariable UUID cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCard(@PathVariable UUID cardId) {
        cardService.deleteCard(cardId);
    }

    @GetMapping("/{cardId}")
    public CardDto getCardById(@PathVariable UUID cardId) {
        return cardService.getCardById(cardId);
    }

    @GetMapping
    public List<CardDto> getAllCards(@Valid Pageable pageable) {
        return cardService.getAllCards(pageable);
    }
}
