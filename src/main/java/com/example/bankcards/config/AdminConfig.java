package com.example.bankcards.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация администратора приложения.
 * Настраивает имя и пароль для администратора приложения, который добавляется в базу данных при первом запуске.
 */
@Configuration
@ConfigurationProperties(prefix = "app.admin")
@Getter
@Setter
public class AdminConfig {
    /**
     * Имя администратора
     */
    private String name;
    /**
     * Пароль администратора
     */
    private String password;
}
