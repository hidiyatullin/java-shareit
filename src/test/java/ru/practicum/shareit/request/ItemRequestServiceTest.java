package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;
    private UserService userService;
    private ItemRequestService itemRequestService;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private Item item1;
    private Item item2;
    private ItemRequestDto itemRequestDto1;
    private ItemRequest itemRequest1;
    private ItemRequestDto requestDtoNew;

    @Autowired
    public ItemRequestServiceTest(UserRepository userRepository, ItemRepository itemRepository,
                                  ItemRequestRepository itemRequestRepository, ItemService itemService,
                             UserService userService, ItemRequestService itemRequestService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.itemRequestService = itemRequestService;
    }

    @BeforeEach
    public void start() {
        LocalDateTime startLastBooking = LocalDateTime.now().minusDays(7);
        LocalDateTime endLastBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime startNextBooking = LocalDateTime.now().plusDays(3);
        LocalDateTime endNextBooking = LocalDateTime.now().plusDays(7);
        ItemDto.BookingDto lastBooking = new ItemDto.BookingDto(2L, startLastBooking, endLastBooking, 1L);
        ItemDto.BookingDto nextBooking = new ItemDto.BookingDto(3L, startNextBooking, endNextBooking, 2L);

        userDto1 = new UserDto(1L, "userDtoname1", "userDto1Email@email");
        userDto2 = new UserDto(2L, "userDtoname2", "userDto2Email@email");

        user1 = UserMapper.toUser(userDto1);
        user2 = UserMapper.toUser(userDto2);

        itemDto1 = new ItemDto(1L, "itemName", "itemDescription", true, null, lastBooking,
                nextBooking, null);
        item1 = ItemMapper.toItem(itemDto1, user1);
        ItemRequestDto.ItemAnswerDto itemAnswerDto =
                new ItemRequestDto.ItemAnswerDto(3L, "itemAnswer1", "itemAnswerDescription1",
                        user1.getId(), true, user2.getId());
        itemRequestDto1 = new ItemRequestDto(1L, "requestDescription1", null, LocalDateTime.now(), List.of(itemAnswerDto));
        itemRequest1 = ItemRequestMapper.toItemRequest(itemRequestDto1);

        userRepository.save(user1);
        userRepository.save(user2);
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        itemRequestRepository.save(itemRequest1);
        requestDtoNew = itemRequestService.create(itemRequestDto1, user2.getId());
        item1.setItemRequest(itemRequest1);
        itemRepository.save(item1);
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        itemDto1.setRequestId(1L);
        itemService.createItem(itemDto1, user1.getId());
        itemRequestRepository.save(itemRequest1);
    }

    @Test
    public void create() {
        assertEquals(requestDtoNew.getId(), itemRequest1.getId());
    }

    @Test
    public void createWrongUser() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> itemRequestService.create(itemRequestDto1, 99L));
        assertEquals("Нет такого пользователя", exception.getMessage());
    }

    @Test
    public void getAll() {
        List<ItemRequestDto> requestsByCreator = itemRequestService.getAllItemRequestsOfUser(user2.getId());
        assertEquals(requestsByCreator.size(), 1);
    }

    @Test
    public void getById() {
        ItemRequestDto itemRequestDtoNew = itemRequestService.getByRequestId(user2.getId(), itemRequest1.getId());
        assertEquals(itemRequestDtoNew.getId(), itemRequest1.getId());
    }

    @Test
    public void getAllByPage() {
        List<ItemRequestDto> requestsByCreator = itemRequestService.getAllItemRequests(user1.getId(), 1, 10);
        assertEquals(requestsByCreator.size(), 1);
    }

    @Test
    public void getAllByPageWithWrong() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequests(user1.getId(), -1, -10));
        assertEquals("не верно указан количество позиций на странице", exception.getMessage());
    }
}