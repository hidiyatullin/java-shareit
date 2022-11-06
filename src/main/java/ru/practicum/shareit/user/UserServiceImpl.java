package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.updateUser(user, userId));
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.getUser(userId));
    }

    public List<UserDto> getUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user : userRepository.getUsers()) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

}
