package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;

import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    private final UserController userController;
    private UserDto userDto1;
    private UserDto userDto2;

    @Autowired
    public UserControllerTest(UserController userController) {
        this.userController = userController;
    }

    @BeforeEach
    public void start() {
        userDto1 = new UserDto(1L, "user1", "user1@user1.ru");
        userDto2 = new UserDto(2L, "user2", "user1@user2.ru");
    }

    @Test
    public void create() {
        UserDto userDtoNew1 = userController.createUser(userDto1);
        assertEquals(userDto1.getName(), userDtoNew1.getName());
    }

    @Test
    public void getAll() {
        userController.createUser(userDto1);
        userController.createUser(userDto2);
        List<UserDto> userDtos = userController.getUsers();
        assertEquals(userDtos.size(), 2);
    }

    @Test
    public void getById() {
        UserDto userDtoNew1 = userController.createUser(userDto1);
        UserDto userDtoNew2 = userController.createUser(userDto2);
        UserDto userDtoNew = userController.getUser(1L);
        assertEquals(userDtoNew.getId(), userDtoNew1.getId());
    }

    @Test
    public void update() {
        UserDto userDtoNew1 = userController.createUser(userDto1);
        userDtoNew1 = userController.updateUser(1L, userDto2);
        assertEquals(userDtoNew1.getName(), userDto2.getName());
    }

    @Test
    public void delete() {
        UserDto userDtoNew1 = userController.createUser(userDto1);
        UserDto userDtoNew2 = userController.createUser(userDto2);
        userController.deleteUser(1L);
        assertEquals(userController.getUsers().size(), 1);
    }
}