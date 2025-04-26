package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.NameAlreadyExistException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        log.debug("\n[JSON] - UserDto: \n name: {} \n email: {}", userDto.getName(), userDto.getEmail());
        if (userRepository.existsByName(userDto.getName())) {
            log.warn(String.format("Can't create user with name: %s, the name was used by another user", userDto.getName()));
            throw new NameAlreadyExistException(String.format("Can't create user with name: %s, the name was used by another user",
                    userDto.getName()));
        }
        log.debug(String.format("The user with name %s was created", userDto.getName()));
        User saveUser = userMapper.toUserModel(userDto);
        log.debug("saveUser after UserMapper: \n name: {} \n email: {}", saveUser.getName(), saveUser.getEmail());
        return userMapper.toUserDto(userRepository.save(saveUser));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.debug("Received users");
        Pageable page = PageRequest.of(from / size, size);

        if (ids == null || ids.isEmpty()) {
            return userMapper.toUserDtoList(userRepository.findAll(page).toList());
        } else {
            return userMapper.toUserDtoList(userRepository.findAllById(ids));
        }

    }

    @Override
    public void deleteUser(Long id) {
        log.debug("User with id: {} was deleted ", id);
        userRepository.deleteById(id);
    }
}