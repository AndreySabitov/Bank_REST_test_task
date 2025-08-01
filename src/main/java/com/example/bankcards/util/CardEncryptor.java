package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

@Component
public class CardEncryptor {
    private final TextEncryptor encryptor;

    public CardEncryptor(@Value("${encryptor.password}") String password) {
        String salt = KeyGenerators.string().generateKey();
        this.encryptor = Encryptors.text(password, salt);
    }

    public String encrypt(String cardNumber) {
        return encryptor.encrypt(cardNumber);
    }

    public String decrypt(String encryptedCardNumber) {
        return encryptor.decrypt(encryptedCardNumber);
    }
}
