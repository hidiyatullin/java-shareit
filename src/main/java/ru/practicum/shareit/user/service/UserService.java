package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto itemDto);

    UserDto updateUser(UserDto userDto, long userId);

    UserDto getUser(Long userId);

    List<UserDto> getUsers();

    void deleteUser(Long userId);
}
