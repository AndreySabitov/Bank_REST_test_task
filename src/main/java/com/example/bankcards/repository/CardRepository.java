package com.example.bankcards.repository;

import com.example.bankcards.entity.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью {@link Card} в базе данных.
 * Предоставляет стандартные методы CRUD-операций через {@link JpaRepository}
 * и возможность построения динамических запросов через {@link QuerydslPredicateExecutor}.
 */
public interface CardRepository extends JpaRepository<Card, UUID>, QuerydslPredicateExecutor<Card> {
    /**
     * Проверяет существование карты с указанным хешем номера в системе.
     * <p>
     * Метод выполняет поиск в базе данных с использованием хэшированного значения номера карты,
     * что обеспечивает безопасное хранение конфиденциальных данных.
     *
     * @param hash хеш-значение номера карты
     * @return {@code true} если карта с указанным хешем существует в базе данных,
     *         {@code false} в противном случае
     */
    boolean existsByHash(String hash);
}