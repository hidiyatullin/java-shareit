package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.ItemNotFoundException;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestRepository;
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
class ItemServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;
    private UserService userService;
    private BookingService bookingService;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;
    private ItemDto itemDto1;

    private Item item1;

    private ItemDto itemDtoNew;
    private BookingDto bookingDto1;
    private CommentDto commentDto1;
    private Comment comment1;

    @Autowired
    public ItemServiceTest(UserRepository userRepository, ItemRepository itemRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository,
                           ItemRequestRepository itemRequestRepository, ItemService itemService,
                           UserService userService, BookingService bookingService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @BeforeEach
    public void start() {
        LocalDateTime startLastBooking = LocalDateTime.now().minusDays(7);
        LocalDateTime endLastBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime startNextBooking = LocalDateTime.now().plusDays(3);
        LocalDateTime endNextBooking = LocalDateTime.now().plusDays(7);
        ItemDto.BookingDto lastBooking = new ItemDto.BookingDto(1L, startLastBooking, endLastBooking, 1L);
        ItemDto.BookingDto nextBooking = new ItemDto.BookingDto(2L, startNextBooking, endNextBooking, 2L);

        userDto1 = new UserDto(1L, "userDtoname1", "userDto1Email@email");
        userDto2 = new UserDto(2L, "userDtoname2", "userDto2Email@email");

        user1 = UserMapper.toUser(userDto1);
        user2 = UserMapper.toUser(userDto2);

        itemDto1 = new ItemDto(1L, "itemName", "itemDescription", true, null, lastBooking,
                nextBooking, null);
        item1 = ItemMapper.toItem(itemDto1, user1);
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        itemDtoNew = itemService.createItem(itemDto1, user1.getId());
        BookingInputDto bookingInputDto = new BookingInputDto(1L, item1.getId(), LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(7));
        Booking booking = BookingMapper.toBooking(bookingInputDto);
        bookingDto1 = BookingMapper.toDto(booking);
        bookingRepository.save(booking);
        bookingService.create(bookingInputDto, user2.getId());

        commentDto1 = new CommentDto(1L, "CommentText1", item1.getId(), user2.getName(), LocalDateTime.now());
        comment1 = CommentMapper.toComment(commentDto1);
    }

    @Test
    public void create() {
        assertEquals(itemDtoNew.getId(), itemDto1.getId());
    }

    @Test
    public void addCommentWithoutBookingByUser() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addComment(user2.getId(), itemDtoNew.getId(), commentDto1));
        assertEquals("Пользователь с id 2не бронировал вещь с id 1", exception.getMessage());
    }

    @Test
    public void addComment() {
        BookingInputDto bookingInputDto3 = new BookingInputDto(3L, item1.getId(), LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(4));
        Booking booking3 = BookingMapper.toBooking(bookingInputDto3);
        bookingRepository.save(booking3);
        bookingService.create(bookingInputDto3, user2.getId());
        CommentDto commentDto = itemService.addComment(user2.getId(), item1.getId(), commentDto1);
        assertEquals(1L, commentDto.getId());
    }

    @Test
    public void addCommentWithoutText() {
        commentDto1.setText("");
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addComment(user2.getId(), itemDtoNew.getId(), commentDto1));
        assertEquals("нет текста комментария", exception.getMessage());
    }

    @Test
    public void getAll() {
        BookingInputDto bookingInputDto3 = new BookingInputDto(3L, item1.getId(), LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(4));
        Booking booking3 = BookingMapper.toBooking(bookingInputDto3);
        bookingRepository.save(booking3);
        bookingService.create(bookingInputDto3, user2.getId());
        CommentDto commentDto = itemService.addComment(user2.getId(), item1.getId(), commentDto1);
        assertEquals(1, itemService.getItems(user1.getId(), 1, 10).size());
    }

    @Test
    public void getAllWithWrong() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.getItems(user1.getId(), -1, -2));
        assertEquals(exception.getMessage(), "не верно указан количество позиций на странице");
    }

    @Test
    public void getById() {
        BookingInputDto bookingInputDto3 = new BookingInputDto(3L, item1.getId(), LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(4));
        Booking booking3 = BookingMapper.toBooking(bookingInputDto3);
        bookingRepository.save(booking3);
        bookingService.create(bookingInputDto3, user2.getId());
        CommentDto commentDto = itemService.addComment(user2.getId(), item1.getId(), commentDto1);
        assertEquals(itemService.getItem(user1.getId(), itemDto1.getId()).getId(), 1L);
    }

    @Test
    public void getByIdWithWrongItemId() {
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> itemService.getItem(99L, user1.getId()).getId());

        assertEquals(exception.getMessage(), "Товар с id " + 99L + " не зарегестрирован");
    }

    @Test
    public void update() {
        ItemDto itemDtoNew = new ItemDto(1L, "updateItemDto", null, true, null, null, null, null);
        ItemDto itemDtoupdate = itemService.updateItem(itemDtoNew, user1.getId(), item1.getId());
        assertEquals(itemDtoupdate.getName(), "updateItemDto");
    }

    @Test
    public void updateWithEmtyItem() {
        ItemDto itemDtoNew = new ItemDto(1L, null, "updateDescription", null, null, null, null, null);
        ItemDto itemDtoupdate = itemService.updateItem(itemDtoNew, user1.getId(), item1.getId());
        assertEquals(itemDtoupdate.getDescription(), "updateDescription");
    }

    @Test
    public void search() {
        List<ItemDto> itemDescriptionAfterSearch = itemService.findItems("itemdescrip", 1, 10);
        assertEquals(itemDescriptionAfterSearch.size(), 1);
    }

    @Test
    public void searchWithEmptyText() {
        List<ItemDto> itemDescriptionAfterSearch = itemService.findItems("", 1, 10);
        assertEquals(itemDescriptionAfterSearch.size(), 0);
    }

    @Test
    public void searchWithWrongPage() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.findItems("itemdescrip", -1, -10));
        assertEquals("не верно указан количество позиций на странице", exception.getMessage());
    }
}