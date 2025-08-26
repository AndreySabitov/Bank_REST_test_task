package com.example.bankcards.repository;

import com.example.bankcards.entity.request.BlockingCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью BlockingCardRequest в базе данных
 */
public interface BlockingCardRequestRepository extends JpaRepository<BlockingCardRequest, UUID> {
    /**
     * Проверяет, существует ли запрос на блокировку по идентификатору карты и идентификатору пользователя
     *
     * @param cardId      идентификатор карты (UUID)
     * @param initiatorId идентификатор пользователя, который инициировал блокировку карты (UUID)
     * @return {@code true} если запрос существует, {@code false} в противном случае
     */
    boolean existsByCardIdAndInitiatorId(UUID cardId, UUID initiatorId);
}