package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
import com.example.bankcards.dto.card.Pageable;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.exception.ValidationException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.card.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CardDto createCard(CreateCardDto createCardDto) {
        User owner = userRepository.findById(createCardDto.getUserId()).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        return CardMapper.mapToDto(cardRepository.save(CardMapper.mapCreateDtoToCard(createCardDto, owner)));
    }

    @Override
    @Transactional
    public boolean blockingCard(UUID cardId) {
        Card card = findCardById(cardId);

        if (card.getStatus().equals(CardStatus.BLOCKED)) {
            log.info("Карта уже заблокирована");
            return true;
        }

        card.setStatus(CardStatus.BLOCKED);

        return true;
    }

    @Override
    @Transactional
    public boolean activateCard(UUID cardId) {
        Card card = findCardById(cardId);

        if (card.getStatus().equals(CardStatus.EXPIRED)) {
            throw new ValidationException("Нельзя активировать карту срок действия которой истек");
        }
        if (card.getStatus().equals(CardStatus.ACTIVE)) {
            log.info("Карта уже активна");
            return true;
        }

        card.setStatus(CardStatus.ACTIVE);

        return true;
    }

    @Override
    @Transactional
    public void deleteCard(UUID cardID) {
        if (cardRepository.existsById(cardID)) {
            cardRepository.deleteById(cardID);
        }
    }

    @Override
    public CardDto getCardById(UUID cardId) {
        return CardMapper.mapToDto(findCardById(cardId));
    }

    @Override
    public List<CardDto> getAllCards(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize());

        return cardRepository.findAll(pageRequest).stream()
                .map(CardMapper::mapToDto)
                .toList();
    }

    private Card findCardById(UUID cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException("Карта не найдена"));
    }
}
