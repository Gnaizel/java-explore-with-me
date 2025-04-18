package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserShortDto toShortDto(User user);

    User toUserModel(UserDto userModelDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> usersList);
}