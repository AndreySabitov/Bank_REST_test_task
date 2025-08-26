package com.example.bankcards.util;

import com.example.bankcards.config.AdminConfig;
import com.example.bankcards.entity.user.Role;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Компонент для автоматической инициализации учётной записи администратора при старте приложения.
 * <p>
 * Выполняет проверку существования администратора в базе данных и при необходимости создаёт
 * новую учётную запись с правами {@link Role#ROLE_ADMIN}, используя параметры из {@link AdminConfig}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final AdminConfig adminConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Инициализирует учётную запись администратора при готовности приложения.
     * <p>
     * Метод выполняет следующие действия:
     * <ul>
     *   <li>Проверяет существование пользователя с именем администратора
     *   <li>Создаёт нового пользователя с ролью администратора, если проверка не пройдена</li>
     *   <li>Хеширует пароль администратора с помощью {@link PasswordEncoder} перед сохранением</li>
     *   <li>Сохраняет новую запись в базу данных и логирует результат операции</li>
     * </ul>
     *
     * <p>Запуск метода привязан к событию {@link ApplicationReadyEvent} для гарантии корректной работы
     * с полноценно инициализированным контекстом Spring.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initAdmin() {
        if (!userRepository.existsByName(adminConfig.getName())) {
            User admin = User.builder()
                    .name(adminConfig.getName())
                    .role(Role.ROLE_ADMIN)
                    .password(passwordEncoder.encode(adminConfig.getPassword()))
                    .build();

            userRepository.save(admin);
            log.info("Сохранили администратора в базу данных");
        }
    }
}
