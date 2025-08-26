package com.example.bankcards.entity.request;

import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Сущность, описывающая запрос на блокировку карты пользователем.
 * <p>
 * Содержит информацию:
 * <li>Ссылка на блокируемую карту {@link Card}</li>
 * <li>Ссылка на инициатора блокировки {@link User}</li>
 * <li>Статус запроса на блокировку {@link BlockingCardStatus}</li>
 */
@Entity
@Table(name = "blocking_card_requests")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BlockingCardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Enumerated(EnumType.STRING)
    private BlockingCardStatus state;
}
