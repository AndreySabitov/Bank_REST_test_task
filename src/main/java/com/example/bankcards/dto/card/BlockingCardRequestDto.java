package com.example.bankcards.dto.card;

import com.example.bankcards.entity.request.BlockingCardStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BlockingCardRequestDto {
    private UUID id;
    private UUID cardId;
    private UUID initiatorId;
    private BlockingCardStatus state;
}
