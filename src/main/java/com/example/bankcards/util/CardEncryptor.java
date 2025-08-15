package com.example.bankcards.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CardEncryptor {
    private final TextEncryptor encryptor;

    public CardEncryptor(@Value("${encryptor.password}") String password, @Value("${encryptor.salt}") String salt) {
        this.encryptor = Encryptors.text(password, salt);
    }

    public String encrypt(String cardNumber) {
        return encryptor.encrypt(cardNumber);
    }

    public String decrypt(String encryptedCardNumber) {
        return encryptor.decrypt(encryptedCardNumber);
    }
}
