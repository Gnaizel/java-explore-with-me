package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

@Component
public class UserMapper {
    public UserShortDto toShortDto(User user) {
        if (user == null) return null;
        return new UserShortDto(user.getId(), user.getName());
    }
}