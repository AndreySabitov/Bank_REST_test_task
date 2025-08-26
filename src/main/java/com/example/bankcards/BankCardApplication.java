package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение.
 * Запускает Spring Boot-контекст и выполняет инициализацию системы.
 *
 * @author Andrey Sabitov
 */
@SpringBootApplication
public class BankCardApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankCardApplication.class, args);
    }
}
