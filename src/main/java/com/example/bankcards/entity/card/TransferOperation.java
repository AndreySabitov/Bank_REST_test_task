package com.example.bankcards.entity.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сущность, описывающая перевод денежных средств между двумя картами.
 * <p>
 * Содержит информацию о переводе:
 * <li>Идентификатор исходной карты</li>
 * <li>Идентификатор целевой карты</li>
 * <li>Сумма перевода</li>
 */
@Entity
@Table(name = "transfer_operations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_card_id")
    private Card fromCard;

    @ManyToOne
    @JoinColumn(name = "to_card_id")
    private Card toCard;

    private BigDecimal amount;
}
