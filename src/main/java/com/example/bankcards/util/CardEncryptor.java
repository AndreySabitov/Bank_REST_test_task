package com.example.bankcards.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

/**
 * Компонент для шифрования и дешифрования номеров банковских карт.
 * <p>
 * Обеспечивает безопасное хранение конфиденциальных данных с использованием симметричного шифрования.
 * Настройки шифрования (пароль и соль) задаются через конфигурацию приложения.
 */
@Slf4j
@Component
public class CardEncryptor {
    private final TextEncryptor encryptor;

    /**
     * Инициализирует компонент шифрования с заданными параметрами.
     *
     * @param password ключ шифрования, заданный в конфигурации приложения
     * @param salt соль для усиления безопасности шифрования
     */
    public CardEncryptor(@Value("${encryptor.password}") String password, @Value("${encryptor.salt}") String salt) {
        this.encryptor = Encryptors.text(password, salt);
    }

    /**
     * Шифрует номер карты.
     *
     * @param cardNumber исходный номер карты в формате строки
     * @return зашифрованная версия номера карты
     */
    public String encrypt(String cardNumber) {
        return encryptor.encrypt(cardNumber);
    }

    /**
     * Дешифрует зашифрованный номер карты.
     *
     * @param encryptedCardNumber зашифрованная строка, полученная методом {@link #encrypt(String)}
     * @return исходный номер карты в формате строки
     */
    public String decrypt(String encryptedCardNumber) {
        return encryptor.decrypt(encryptedCardNumber);
    }
}
