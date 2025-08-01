package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CardConcealer {

    public String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(12);
    }

}
