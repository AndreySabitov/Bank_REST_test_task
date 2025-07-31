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
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
    private final CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto createCard(@Valid @RequestBody CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }

    @PatchMapping("/blocking")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void blockingCard(@RequestBody UUID cardId) {
        cardService.blockingCard(cardId);
    }

    @PatchMapping("/blocking/byRequest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void blockingCardByRequest(@RequestBody UUID requestId) {
        cardService.blockingCardByRequest(requestId);
    }

    @PatchMapping("/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateCard(@RequestBody UUID cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping
    public void deleteCard(@RequestBody UUID cardId) {
        cardService.deleteCard(cardId);
    }

    @GetMapping
    public CardDto getCardById(@RequestBody UUID cardId) {
        return cardService.getCardById(cardId);
    }

    @GetMapping("/all")
    public List<CardDto> getAllCards(@Valid Pageable pageable) {
        return cardService.getAllCards(pageable);
    }
}
