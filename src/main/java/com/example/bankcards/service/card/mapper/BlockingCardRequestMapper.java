package com.example.bankcards.service.card.mapper;

import com.example.bankcards.dto.card.BlockingCardRequestDto;
import com.example.bankcards.entity.request.BlockingCardRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BlockingCardRequestMapper {

    public BlockingCardRequestDto mapToDto(BlockingCardRequest request) {
        return BlockingCardRequestDto.builder()
                .id(request.getId())
                .cardId(request.getCard().getId())
                .initiatorId(request.getInitiator().getId())
                .state(request.getState())
                .build();
    }
}
