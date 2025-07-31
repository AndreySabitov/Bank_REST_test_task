package com.example.bankcards.service.user.mapper;

import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.user.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
