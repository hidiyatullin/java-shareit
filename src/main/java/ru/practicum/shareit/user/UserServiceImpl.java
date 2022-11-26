package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
//        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
//        return UserMapper.toUserDto(userRepository.updateUser(user, userId));
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).get());
//        return UserMapper.toUserDto(userRepository.getUser(userId));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
//        return userRepository.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
//        userRepository.deleteUser(userId);
    }

}
