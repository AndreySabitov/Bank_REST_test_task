package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CardConcealer {

    public String maskCardNumber(String encryptedCardNumber) {
        String cardNumber = CardEncryptor.decrypt(encryptedCardNumber);

        return "**** **** **** " + cardNumber.substring(12);
    }
}
