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

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final AdminConfig adminConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
