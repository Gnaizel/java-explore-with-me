package ru.practicum.service.user;

import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userModelDto);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}