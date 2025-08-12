package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.request.BlockingCardRequest;
import com.example.bankcards.entity.request.BlockingCardStatus;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.exception.ValidationException;
import com.example.bankcards.repository.BlockingCardRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class CardServiceImplTest {
    private final CardService cardService;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    private final User user = User.builder()
            .name("name")
            .email("name@mail.ru")
            .role(Role.ROLE_USER)
            .password("password")
            .build();

    private final Card card = Card.builder()
            .maskedCardNumber("**** **** **** 4444")
            .encryptedCardNumber("encrypted_card_number")
            .status(CardStatus.ACTIVE)
            .owner(user)
            .expirationDate(LocalDate.now().plusYears(10))
            .balance(BigDecimal.valueOf(100.0))
            .build();

    @Autowired
    private BlockingCardRequestRepository blockingCardRequestRepository;

    @Test
    void checkThrowNotFoundException_IfTryCreateCardAndUserNotExists() {
        assertThrows(NotFoundException.class, () ->
                cardService.createCard(CreateCardDto.builder()
                        .userId(UUID.randomUUID())
                        .cardNumber("5555555555554444")
                        .build()));
    }

    @Test
    void checkCanCreateCard() {
        User owner = userRepository.save(user);

        CardDto cardDto = cardService.createCard(CreateCardDto.builder()
                .cardNumber("5555555555554444")
                .userId(owner.getId())
                .build());

        assertNotNull(cardDto);
        assertEquals("**** **** **** 4444", cardDto.getMaskedCardNumber());
        assertEquals(owner.getName(), cardDto.getUsername());
        assertEquals(BigDecimal.valueOf(0.0), cardDto.getBalance());
        assertEquals(LocalDate.now().plusYears(10), cardDto.getExpirationDate());
    }

    @Test
    void checkThrowNotFoundException_IfTryBlockingNotExistsCard() {
        assertThrows(NotFoundException.class, () -> cardService.blockingCard(UUID.randomUUID()));
    }

    @Test
    void checkCanBlockingCard() {
        Card card1 = cardRepository.save(card);

        cardService.blockingCard(card1.getId());

        Card blockingCard = cardRepository.findById(card1.getId()).orElseThrow();

        assertEquals(card1.getId(), blockingCard.getId());
        assertEquals(CardStatus.BLOCKED, blockingCard.getStatus());
    }

    @Test
    void checkThrowValidationException_IfTryActivateExpiredCard() {
        card.setStatus(CardStatus.EXPIRED);
        Card card1 = cardRepository.save(card);

        assertThrows(ValidationException.class, () -> cardService.activateCard(card1.getId()));
    }

    @Test
    void checkCanActivateCard() {
        card.setStatus(CardStatus.BLOCKED);
        Card card1 = cardRepository.save(card);

        cardService.activateCard(card1.getId());
        Card activatedCard = cardRepository.findById(card1.getId()).orElseThrow();

        assertEquals(card1.getId(), activatedCard.getId());
        assertEquals(CardStatus.ACTIVE, activatedCard.getStatus());
    }

    @Test
    void checkThrowNotFoundException_IfTryGetNitExistsCard() {
        assertThrows(NotFoundException.class, () -> cardService.getCardById(UUID.randomUUID()));
    }

    @Test
    void checkCanGetCardById() {
        Card card1 = cardRepository.save(card);

        CardDto cardDto = cardService.getCardById(card1.getId());

        assertNotNull(cardDto);
        assertEquals(card1.getId(), cardDto.getId());
        assertEquals(card1.getOwner().getName(), cardDto.getUsername());
        assertEquals(card1.getBalance(), cardDto.getBalance());
        assertEquals(card1.getExpirationDate(), cardDto.getExpirationDate());
    }

    @Test
    void checkCanDeleteCard() {
        userRepository.save(user);
        Card card1 = cardRepository.save(card);

        cardService.deleteCard(card1.getId());

        assertThrows(NotFoundException.class, () -> cardService.getCardById(card1.getId()));
    }

    @Test
    void checkReturnEmptyList_IfGetAllCardsButCardsNotExists() {
        List<CardDto> cardDtoList = cardService.getAllCards(new Pageable(0, 10));

        assertTrue(cardDtoList.isEmpty());
    }

    @Test
    void checkCanGetAllCards() {
        userRepository.save(user);
        cardRepository.save(card);

        List<CardDto> cardDtoList = cardService.getAllCards(new Pageable(0, 10));

        assertFalse(cardDtoList.isEmpty());
        assertEquals(1, cardDtoList.size());
    }

    @Test
    void checkThrowNotFoundException_IfTryGetAllCardsByUserButUserNotExists() {
        assertThrows(NotFoundException.class, () -> cardService.getAllCardsByUser(new Pageable(0, 10), null));
    }

    @Test
    void checkCanGetAllCardsByUser() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        Card card1 = cardRepository.save(card);

        List<CardDto> cardDtos = cardService.getAllCardsByUser(new Pageable(0, 10), null);

        assertFalse(cardDtos.isEmpty());
        assertEquals(1, cardDtos.size());
        assertEquals(card1.getId(), cardDtos.getFirst().getId());
    }

    @Test
    void checkCanGetAllCardsByUserAndStatus() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        Card card1 = cardRepository.save(card);
        cardRepository.save(Card.builder()
                .balance(BigDecimal.valueOf(0.0))
                .expirationDate(LocalDate.now().plusYears(10))
                .owner(user1)
                .status(CardStatus.BLOCKED)
                .encryptedCardNumber("encrypted_card_other_number")
                .maskedCardNumber("**** **** **** 4343")
                .build());

        List<CardDto> cardDtos = cardService.getAllCardsByUser(new Pageable(0, 10), CardStatus.ACTIVE);

        assertFalse(cardDtos.isEmpty());
        assertEquals(1, cardDtos.size());
        assertEquals(card1.getId(), cardDtos.getFirst().getId());
    }

    @Test
    void checkThrowNotFoundException_IfTryAddBlockingRequestAndUserNotExist() {
        assertThrows(NotFoundException.class, () -> cardService.addBlockingCardRequest(UUID.randomUUID()));
    }

    @Test
    void checkThrowNotFoundException_IfTryAddBlockingRequestToNotExistsCard() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        assertThrows(NotFoundException.class, () -> cardService.addBlockingCardRequest(UUID.randomUUID()));
    }

    @Test
    void checkThrowValidationException_IfTryAddBlockingRequestToNotActiveCard() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        card.setStatus(CardStatus.BLOCKED);
        Card card1 = cardRepository.save(card);

        assertThrows(ValidationException.class, () -> cardService.addBlockingCardRequest(card1.getId()));
    }

    @Test
    void checkThrowValidationException_IfTryAddBlockingRequestSecondTime() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        Card card1 = cardRepository.save(card);

        cardService.addBlockingCardRequest(card1.getId());
        assertThrows(ValidationException.class, () -> cardService.addBlockingCardRequest(card1.getId()));
    }

    @Test
    void checkCanAddBlockingCardRequest() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        Card card1 = cardRepository.save(card);

        BlockingCardRequestDto dto = cardService.addBlockingCardRequest(card1.getId());

        assertEquals(card1.getId(), dto.getCardId());
        assertEquals(user1.getId(), dto.getInitiatorId());
        assertEquals(BlockingCardStatus.WAITING, dto.getState());
    }

    @Test
    void checkCanBlockingCardByRequest() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));
        Card card1 = cardRepository.save(card);

        BlockingCardRequestDto dto = cardService.addBlockingCardRequest(card1.getId());

        cardService.blockingCardByRequest(dto.getId());

        BlockingCardRequest completedRequest = blockingCardRequestRepository.findById(dto.getId()).orElseThrow();

        assertEquals(card1.getId(), dto.getCardId());
        assertEquals(user1.getId(), dto.getInitiatorId());
        assertEquals(BlockingCardStatus.COMPLETED, completedRequest.getState());
        assertEquals(CardStatus.BLOCKED, card1.getStatus());
    }

    @Test
    void checkThrowNotFoundException_IfTryBlockingCardByRequestAndRequestNotExists() {
        assertThrows(NotFoundException.class, () -> cardService.blockingCardByRequest(UUID.randomUUID()));
    }

    @Test
    void checkThrowNotFoundException_IfTryTransferAndFromCardNotExists() {
        assertThrows(NotFoundException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowNotFoundException_IfTryTransferAndToCardNotExists() {
        Card card1 = cardRepository.save(card);

        assertThrows(NotFoundException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), UUID.randomUUID(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowValidationException_IfTryTransferAndFromCardIsBlocked() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        card.setStatus(CardStatus.BLOCKED);
        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        assertThrows(ValidationException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), card2.getId(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowValidationException_IfTryTransferAndToCardIsBlocked() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.BLOCKED)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        assertThrows(ValidationException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), card2.getId(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowValidationException_IfTryTransferAndFromCardIsExpired() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        card.setStatus(CardStatus.EXPIRED);
        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        assertThrows(ValidationException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), card2.getId(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowValidationException_IfTryTransferAndToCardIsExpired() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.EXPIRED)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        assertThrows(ValidationException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), card2.getId(), BigDecimal.valueOf(100.0))));
    }

    @Test
    void checkThrowValidationException_IfTryTransferAndFromCardNotHaveMoney() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        assertThrows(ValidationException.class, () -> cardService.transferBetweenCards(
                new TransferBetweenCardsRequest(card1.getId(), card2.getId(), BigDecimal.valueOf(200.0))));
    }

    @Test
    void checkCanTransferMoney() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        Card card1 = cardRepository.save(card);
        Card card2 = cardRepository.save(Card.builder()
                .maskedCardNumber("**** **** **** 7896")
                .encryptedCardNumber("other_encrypted_card_number")
                .owner(user)
                .status(CardStatus.ACTIVE)
                .expirationDate(LocalDate.now().plusYears(10))
                .balance(BigDecimal.valueOf(100.0))
                .build());

        cardService.transferBetweenCards(new TransferBetweenCardsRequest(card1.getId(), card2.getId(),
                BigDecimal.valueOf(50.0)));

        assertEquals(BigDecimal.valueOf(50.0), card1.getBalance());
        assertEquals(BigDecimal.valueOf(150.0), card2.getBalance());
    }

    @Test
    void checkThrowNotFoundException_IfTryGetBalanceButUserNotExists() {
        assertThrows(NotFoundException.class, () -> cardService.getBalance(UUID.randomUUID()));
    }

    @Test
    void checkThrowNotFoundException_IfTryGetBalanceButCardNotExists() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        assertThrows(NotFoundException.class, () -> cardService.getBalance(UUID.randomUUID()));
    }

    @Test
    void checkCanGetBalance() {
        User user1 = userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user1,
                null, user1.getAuthorities()));

        Card card1 = cardRepository.save(card);

        assertEquals(BigDecimal.valueOf(100.0), card1.getBalance());
    }
}