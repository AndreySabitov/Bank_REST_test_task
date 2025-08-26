package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Компонент для генерации криптографических хешей данных.
 * <p>
 * Использует алгоритм HMAC-SHA256 для создания безопасных хешей.
 * Секретный ключ (pepper) задаётся через конфигурацию приложения и служит для усиления безопасности хеширования.
 */
@Component
public class HashEncoder {
    @Value("${app.hashEncoder.pepper}")
    private String pepper;

    /**
     * Генерирует криптографический хеш данных с использованием алгоритма HMAC-SHA256.
     * <p>
     * Метод выполняет следующие действия:
     * <ul>
     * <li>Создаёт экземпляр Mac с алгоритмом HmacSHA256</li>
     * <li>Инициализирует ключ на основе значения pepper</li>
     * <li>Выполняет хеширование входных данных</li>
     * <li>Преобразует результат в шестнадцатеричную строку</li>
     * </ul>
     *
     * @param data входные данные для хеширования
     * @return шестнадцатеричное представление криптографического хеша
     * @throws RuntimeException в случае ошибки при инициализации Mac или ключа
     */
    public String getHash(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(pepper.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(result);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("HMAC-SHA256 error", e);
        }
    }
}
