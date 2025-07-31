package com.example.bankcards.entity.request;

import com.example.bankcards.entity.card.Card;
import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
