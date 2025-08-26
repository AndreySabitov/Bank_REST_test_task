package com.example.bankcards.service.user;

import com.example.bankcards.dto.user.CreateUserRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.User;
import com.example.bankcards.exception.DuplicateException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для управления пользователями
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Создание нового пользователя
     *
     * @param request dto с необходимой информацией для создания нового пользователя ({@link CreateUserRequest})
     * @return {@link UserDto} с информацией о новом пользователе
     */
    @Override
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("Пользователь с таким email уже существует");
        }
        if (userRepository.existsByName(request.getUsername())) {
            throw new DuplicateException("Пользователь с таким именем уже существует");
        }

        return UserMapper.mapToDto(userRepository.save(UserMapper.mapCreateDtoToUser(request)));
    }

    private User getByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    /**
     * Удаление пользователя по ID
     *
     * @param userId идентификатор пользователя ({@link UUID})
     */
    @Override
    public void deleteUserById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    /**
     * Получение пользователя по имени
     *
     * @param username имя пользователя
     * @return {@link UserDetails} с информацией о пользователе
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return getByUsername(username);
    }
}
