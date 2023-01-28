package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTestMockMvc {

    @MockBean
    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    private UserDto userDto1;
    private UserDto userDto2;

    @Autowired
    public UserControllerTestMockMvc(UserService userService, MockMvc mockMvc, ObjectMapper mapper) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @BeforeEach
    void start() {
        userDto1 = new UserDto(1L, "user1", "user1@email.ru");
        userDto2 = new UserDto(2L, "user2", "user2@email.ru");
    }

    @Test
    void create() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto1);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .createUser(userDto1);
    }

    @Test
    void createWrongRequst() throws Exception {
        UserDto userDto3 = new UserDto();
        when(userService.createUser(any()))
                .thenReturn(userDto3);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        Mockito.verify(userService, Mockito.never())
                .createUser(userDto3);
    }

    @Test
    void getAll() throws Exception {
        when(userService.getUsers())
                .thenReturn(List.of(userDto1, userDto2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$[0].email", is(userDto1.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .getUsers();
    }

    @Test
    void getById() throws Exception {
        when(userService.getUser(any()))
                .thenReturn(userDto1);

        mockMvc.perform(get("/users/" + userDto1.getId())
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .getUser(any());
    }

    @Test
    void update() throws Exception {
        when(userService.updateUser(userDto2, userDto1.getId()))
                .thenReturn(userDto2);

        mockMvc.perform(patch("/users/" + userDto1.getId())
                        .content(mapper.writeValueAsString(userDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto2.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto2.getEmail()), String.class));

        Mockito.verify(userService, Mockito.times(1))
                .updateUser(userDto2, userDto1.getId());
    }

    @Test
    void getByIdWithException() throws Exception {
        UserDto userDto99 = new UserDto(99L, "user99", "user99@mail.ru");

        when(userService.getUser(99L))
                .thenThrow(new UserNotFoundException("Пользователя с id " + userDto99.getId() + " не зарегестрирован"));

        mockMvc.perform(get("/users/" + userDto99.getId())
                        .content(mapper.writeValueAsString(userDto99))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }
}