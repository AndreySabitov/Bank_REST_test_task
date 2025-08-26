package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.*;
import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.card.CardStatus;
import com.example.bankcards.entity.card.QCard;
import com.example.bankcards.entity.card.TransferOperation;
import com.example.bankcards.entity.request.BlockingCardRequest;
import com.example.bankcards.entity.request.BlockingCardStatus;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DuplicateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.exception.ValidationException;
import com.example.bankcards.repository.BlockingCardRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferOperationRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.card.mapper.BlockingCardRequestMapper;
import com.example.bankcards.service.card.mapper.CardMapper;
import com.example.bankcards.util.CardConcealer;
import com.example.bankcards.util.CardEncryptor;
import com.example.bankcards.util.HashEncoder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Сервисный класс для управления картами
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final TransferOperationRepository transferOperationRepository;
    private final BlockingCardRequestRepository blockingCardRequestRepository;
    private final CardEncryptor cardEncryptor;
    private final HashEncoder hashEncoder;

    /**
     * Создание новой карты
     *
     * @param createCardDto содержит необходимую информацию для создания новой карты ({@link CreateCardDto})
     * @return {@link CardDto} с информацией о новой карте
     */
    @Override
    @Transactional
    public CardDto createCard(CreateCardDto createCardDto) {
        User owner = userRepository.findById(createCardDto.getUserId()).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        String hash = hashEncoder.getHash(createCardDto.getCardNumber());
        if (cardRepository.existsByHash(hash)) {
            throw new DuplicateException("Карта с таким номером уже существует");
        }

        String encryptedCardNumber = cardEncryptor.encrypt(createCardDto.getCardNumber());
        String maskedCardNumber = CardConcealer.maskCardNumber(createCardDto.getCardNumber());
        Card card = cardRepository.save(CardMapper.mapCreateDtoToCard(owner, encryptedCardNumber, maskedCardNumber, hash));

        return CardMapper.mapToDto(card);
    }

    /**
     * Блокировка карты по ID
     *
     * @param cardId идентификатор блокируемой карты ({@link UUID})
     */
    @Override
    @Transactional
    public void blockingCard(UUID cardId) {
        Card card = findCardById(cardId);

        if (card.getStatus().equals(CardStatus.BLOCKED)) {
            log.info("Карта уже заблокирована");
            return;
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    /**
     * Блокировка карты по запросу пользователя
     *
     * @param requestId идентификатор запроса на блокировку карты ({@link UUID})
     */
    @Override
    @Transactional
    public void blockingCardByRequest(UUID requestId) {
        BlockingCardRequest blockingRequest = blockingCardRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос на блокировку карты не найден"));
        Card card = blockingRequest.getCard();

        if (card.getStatus().equals(CardStatus.BLOCKED)) {
            log.info("Карта уже заблокирована");
            return;
        }
        if (blockingRequest.getState().equals(BlockingCardStatus.COMPLETED)) {
            log.info("Запрос уже выполнен");
            return;
        }

        card.setStatus(CardStatus.BLOCKED);
        blockingRequest.setState(BlockingCardStatus.COMPLETED);
    }

    /**
     * Активация карты по ID
     *
     * @param cardId идентификатор активируемой карты ({@link UUID})
     */
    @Override
    @Transactional
    public void activateCard(UUID cardId) {
        Card card = findCardById(cardId);

        if (card.getStatus().equals(CardStatus.EXPIRED)) {
            throw new ValidationException("Нельзя активировать карту срок действия которой истек");
        }
        if (card.getStatus().equals(CardStatus.ACTIVE)) {
            log.info("Карта уже активна");
            return;
        }

        card.setStatus(CardStatus.ACTIVE);
    }

    /**
     * Удаление карты по ID
     *
     * @param cardID идентификатор удаляемой карты ({@link UUID})
     */
    @Override
    @Transactional
    public void deleteCard(UUID cardID) {
        if (cardRepository.existsById(cardID)) {
            cardRepository.deleteById(cardID);
        } else {
            throw new NotFoundException("Карта не найдена");
        }
    }

    /**
     * Получение информации о карте по ID
     *
     * @param cardId идентификатор карты ({@link UUID})
     * @return {@link CardDto} с информацией о карте
     */
    @Override
    public CardDto getCardById(UUID cardId) {
        return CardMapper.mapToDto(findCardById(cardId));
    }

    /**
     * Получение всех карт
     *
     * @return список {@link CardDto} с информацией о картах
     */
    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(CardMapper::mapToDto)
                .toList();
    }

    /**
     * Получение всех карт пользователя
     *
     * @param pageable   параметры пагинации ({@link Pageable})
     * @param cardStatus статус карт для фильтрации по статусу ({@link CardStatus})
     * @return список {@link CardDto} с информацией по каждой карте
     */
    @Override
    public List<CardDto> getAllCardsByUser(Pageable pageable, CardStatus cardStatus) {
        UUID userId = getCurrentUser().getId();

        PageRequest pageRequest = PageRequest.of(pageable.getPage(), pageable.getSize());

        BooleanExpression conditions = QCard.card.owner.id.eq(userId);

        if (cardStatus != null) {
            conditions = conditions.and(QCard.card.status.eq(cardStatus));
        }

        return cardRepository.findAll(conditions, pageRequest).stream()
                .map(CardMapper::mapToDto)
                .toList();
    }

    /**
     * Создание запроса на блокировку карты пользователем
     *
     * @param cardId идентификатор карты, которую нужно заблокировать ({@link UUID})
     * @return {@link BlockingCardRequestDto} с информацией о созданном запросе на блокировку карты
     */
    @Override
    @Transactional
    public BlockingCardRequestDto addBlockingCardRequest(UUID cardId) {
        User user = getCurrentUser();
        Card card = findCardById(cardId);

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new ValidationException("Карта уже заблокирована или истек срок действия карты");
        }
        if (!card.getOwner().getId().equals(user.getId())) {
            throw new ValidationException("Нельзя подать запрос на блокировку чужой карты");
        }
        if (blockingCardRequestRepository.existsByCardIdAndInitiatorId(cardId, user.getId())) {
            throw new ValidationException("Заявка на блокировку карты уже подана");
        }

        BlockingCardRequest request = BlockingCardRequest.builder()
                .card(card)
                .state(BlockingCardStatus.WAITING)
                .initiator(user)
                .build();

        return BlockingCardRequestMapper.mapToDto(blockingCardRequestRepository.save(request));
    }

    /**
     * Перевод денежных средств между картами пользователя
     *
     * @param transferRequest dto с информацией необходимой для перевода между картами пользователя ({@link TransferBetweenCardsRequest})
     */
    @Override
    @Transactional
    public void transferBetweenCards(TransferBetweenCardsRequest transferRequest) {
        Card fromCard = findCardById(transferRequest.getFromCard());
        Card toCard = findCardById(transferRequest.getToCard());
        User owner = getCurrentUser();
        BigDecimal amount = transferRequest.getAmount();

        if (!fromCard.getOwner().getId().equals(owner.getId()) || !toCard.getOwner().getId().equals(owner.getId())) {
            throw new ValidationException("Обе карты должны принадлежать одному пользователю");
        }
        if (fromCard.getStatus().equals(CardStatus.BLOCKED) || toCard.getStatus().equals(CardStatus.BLOCKED)) {
            throw new ValidationException("Нельзя сделать перевод, если карта заблокирована");
        }
        if (fromCard.getStatus().equals(CardStatus.EXPIRED) || toCard.getStatus().equals(CardStatus.EXPIRED)) {
            throw new ValidationException("Нельзя выполнить перевод, так как прошел срок действия одной из карт");
        }
        if (fromCard.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new ValidationException("Не достаточно средств для перевода");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        transferOperationRepository.save(TransferOperation.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(amount)
                .build());
    }

    /**
     * Получение баланса карты пользователя
     *
     * @param cardId идентификатор карты ({@link UUID})
     * @return баланс карты
     */
    @Override
    public BigDecimal getBalance(UUID cardId) {
        User owner = getCurrentUser();
        Card card = findCardById(cardId);

        if (!card.getOwner().getId().equals(owner.getId())) {
            throw new ValidationException("Нельзя посмотреть баланс чужой карты");
        }

        return card.getBalance();
    }

    private Card findCardById(UUID cardId) {
        log.info("Получаем карту по id = {}", cardId);
        return cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException("Карта не найдена"));
    }

    private User getCurrentUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException e) {
            log.error("Не удалось найти пользователя в контексте");
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
