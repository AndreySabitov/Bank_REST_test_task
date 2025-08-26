package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

/**
 * Утилита для маскировки номеров банковских карт.
 */
@UtilityClass
public class CardConcealer {

    /**
     * Маскирует номер карты, скрывая все цифры, кроме последних четырёх
     *
     * @param cardNumber исходный номер карты
     * @return замаскированный номер карты
     */
    public String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(12);
    }

}
