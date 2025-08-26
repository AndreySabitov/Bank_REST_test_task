package com.example.bankcards.repository;

import com.example.bankcards.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью User в базе данных
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Находит пользователя по точному совпадению имени.
     * <p>
     * Используется для поиска пользователей в системе по их уникальному идентификатору.
     * Регистр символов в имени учитывается при поиске.
     *
     * @param name уникальное имя пользователя в системе
     * @return Optional с найденным пользователем, или пустой Optional, если пользователь не найден
     */
    Optional<User> findByName(String name);

    /**
     * Проверяет существование пользователя с указанным именем.
     *
     * @param name проверяемое имя пользователя
     * @return {@code true} если пользователь с таким именем существует,
     *         {@code false} в противном случае
     */
    boolean existsByName(String name);

    /**
     * Проверяет существование пользователя с указанным email-адресом.
     * <p>
     * Обеспечивает контроль уникальности электронной почты в системе. Регистр символов
     * в email-адресе учитывается при проверке.
     *
     * @param email проверяемый адрес электронной почты
     * @return {@code true} если пользователь с таким email существует,
     *         {@code false} в противном случае
     */
    boolean existsByEmail(String email);
}