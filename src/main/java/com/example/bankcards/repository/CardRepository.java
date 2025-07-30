package com.example.bankcards.repository;

import com.example.bankcards.entity.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}