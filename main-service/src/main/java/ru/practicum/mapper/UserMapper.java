package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserShortDto toShortDto(User user) {
        if (user == null) {
            return null;
        }
        UserShortDto dto = new UserShortDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        // Добавьте другие поля, если они есть в UserShortDto
        return dto;
    }

    public User toUserModel(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }

    public List<UserDto> toUserDtoList(List<User> usersList) {
        if (usersList == null) {
            return Collections.emptyList();
        }
        return usersList.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}