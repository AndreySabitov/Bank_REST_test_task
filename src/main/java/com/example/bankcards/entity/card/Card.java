package com.example.bankcards.entity.card;

import com.example.bankcards.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Сущность, представляющая банковскую карту.
 * <p>
 * Содержит информацию о карте, включая:
 * <li>Зашифрованный номер карты</li>
 * <li>Замаскированный номер карты</li>
 * <li>Срок действия карты</li>
 * <li>Баланс карты</li>
 * <li>Статус карты</li>
 * <li>Ссылка на хозяина карты {@link User}</li>
 * <li>захешированный номер карты, чтобы определять дубликаты</li>
 */
@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "encrypted_card_number", unique = true)
    private String encryptedCardNumber;

    @Column(name = "masked_card_number")
    private String maskedCardNumber;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private String hash;
}
