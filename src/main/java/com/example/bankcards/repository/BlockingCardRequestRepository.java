package com.example.bankcards.repository;

import com.example.bankcards.entity.request.BlockingCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlockingCardRequestRepository extends JpaRepository<BlockingCardRequest, UUID> {
}