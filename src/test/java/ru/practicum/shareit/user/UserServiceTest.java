package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;

    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void start() {
        userDto1 = new UserDto(1L, "userDtoname1", "userDto1Email@email");
        userDto2 = new UserDto(2L, "userDtoname2", "userDto2Email@email");

        user1 = UserMapper.toUser(userDto1);
        user2 = UserMapper.toUser(userDto2);
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    public void create() {
        when(userRepository.save(user1))
                .thenReturn(user1);

        userService.createUser(userDto1);
        UserDto userDtoNew = userService.getUser(userDto1.getId());

        assertEquals(userDtoNew.getId(), userDto1.getId());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(user1);
    }

    @Test
    public void update() {
        userService.createUser(userDto1);
        userService.updateUser(userDto2,1L);
        UserDto userDtoNew = userService.getUser(userDto1.getId());
        assertEquals(userDtoNew.getName(), userDto2.getName());
    }

    @Test
    public void updatewithUserNullParametrs() {
        userRepository.save(user1);
        userService.createUser(userDto1);
        UserDto userDtoNew = new UserDto(2L, null, null);
        UserDto update = userService.updateUser(userDtoNew, userDto1.getId());
    }

    @Test
    public void getById() {
        userRepository.findById(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        userService.createUser(userDto1);
        UserDto userDtoNew = userService.getUser(userDto1.getId());

        assertEquals(userDtoNew.getId(), userDto1.getId());

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    public void getByIdUserNotExist() {
        userRepository.findById(99L);
        when(userRepository.findById(99L))
                .thenReturn(Optional.ofNullable(user1));

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(99L));

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(99L);
    }

    @Test
    public void delete() {
        userService.createUser(userDto1);
        userService.deleteUser(1L);
        assertEquals(userService.getUsers().size(), 0);
    }

    @Test
    public void getAll() {
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        List<UserDto> userDtos = userService.getUsers();
        userRepository.findAll();

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));
        assertEquals(userDtos.size(), 2);

        Mockito.verify(userRepository, Mockito.times(1))
                .findAll();
    }
}