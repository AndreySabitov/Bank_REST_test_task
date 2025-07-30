package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
import com.example.bankcards.dto.card.Pageable;
import com.example.bankcards.service.card.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cards")
public class AdminCardController {
    private final CardService cardService;

    @PostMapping
    public CardDto createCard(@Valid @RequestBody CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }

    @PatchMapping("/blocking")
    public boolean blockingCard(@RequestBody UUID cardId) {
        return cardService.blockingCard(cardId);
    }

    @PatchMapping("/activate")
    public boolean activateCard(@RequestBody UUID cardId) {
        return cardService.activateCard(cardId);
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
