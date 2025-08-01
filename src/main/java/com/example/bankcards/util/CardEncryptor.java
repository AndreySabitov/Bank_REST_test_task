package com.example.bankcards.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

@UtilityClass
public class CardEncryptor {
    @Value("${cardEncryptor.password}")
    private String password;
    private final String salt = KeyGenerators.string().generateKey();
    TextEncryptor encryptor = Encryptors.text(password, salt);

    public String encrypt(String cardNumber) {
        return encryptor.encrypt(cardNumber);
    }

    public String decrypt(String encryptedCardNumber) {
        return encryptor.decrypt(encryptedCardNumber);
    }
}
