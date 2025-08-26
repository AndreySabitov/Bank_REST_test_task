package com.example.bankcards.repository;

import com.example.bankcards.entity.card.TransferOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью {@link TransferOperation} в базе данных
 */
public interface TransferOperationRepository extends JpaRepository<TransferOperation, UUID> {
}