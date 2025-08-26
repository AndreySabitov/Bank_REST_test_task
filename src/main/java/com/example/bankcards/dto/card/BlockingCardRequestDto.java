package com.example.bankcards.dto.card;

import com.example.bankcards.entity.request.BlockingCardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * DTO, который содержит информацию о созданном запросе на блокировку карты.
 */
@Schema(description = "Ответ на запрос блокировки карты пользователем")
@Getter
@Builder
public class BlockingCardRequestDto {
    @Schema(description = "id запроса", example = "fc5855a2-58d8-4148-9a54-277f3bcb60d8")
    private UUID id;
    @Schema(description = "id карты, которую нужно заблокировать", example = "fc5855a2-58d8-4148-9a54-277f3bcb60d8")
    private UUID cardId;
    @Schema(description = "id пользователя, запросившего блокировку", example = "fc5855a2-58d8-4148-9a54-277f3bcb60d8")
    private UUID initiatorId;
    @Schema(description = "Статус запроса")
    private BlockingCardStatus state;
}
