package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.ItemNotFoundException;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
class BookingServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    ItemService itemService;
    UserService userService;
    BookingService bookingService;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;
    private ItemDto itemDto1;
    private Item item1;
    private BookingInputDto bookingInputDto;
    private BookingDto bookingDtoNew;

    @Autowired
    public BookingServiceImplTest(UserRepository userRepository, ItemRepository itemRepository,
                                  BookingRepository bookingRepository, ItemService itemService,
                                  UserService userService, BookingService bookingService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
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
        ItemDto.BookingDto lastBooking = new ItemDto.BookingDto(2L, startLastBooking,
                endLastBooking, 1L);
        ItemDto.BookingDto nextBooking = new ItemDto.BookingDto(3L, startNextBooking,
                endNextBooking, 2L);

        userDto1 = new UserDto(1L, "userDtoname1", "userDto1Email@email");
        userDto2 = new UserDto(2L, "userDtoname2", "userDto2Email@email");

        user1 = UserMapper.toUser(userDto1);
        user2 = UserMapper.toUser(userDto2);

        itemDto1 = new ItemDto(1L, "itemName", "itemDescription", true, null, lastBooking,
                nextBooking, null);
        item1 = ItemMapper.toItem(itemDto1, user1);

        bookingInputDto = new BookingInputDto(1L, item1.getId(), LocalDateTime.now().plusDays(9),
                LocalDateTime.now().plusDays(15));

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        itemService.createItem(itemDto1, user1.getId());
        bookingDtoNew = bookingService.create(bookingInputDto, user2.getId());
    }

    @Test
    public void create() {
        assertEquals(bookingDtoNew.getId(), 1L);
    }

    @Test
    public void createWithWrongItem() {
        BookingInputDto bookingInputDto2 = new BookingInputDto(2L,
                6L, LocalDateTime.now().plusDays(25), LocalDateTime.now().plusDays(35));
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(bookingInputDto2, user2.getId()));
        assertEquals("Вещь с id " + bookingInputDto2.getItemId() + " не найдена", exception.getMessage());
    }

    @Test
    public void createWithWrongBookerId() {
        BookingInputDto bookingInputDto2 = new BookingInputDto(2L,
                item1.getId(), LocalDateTime.now().plusDays(25), LocalDateTime.now().plusDays(35));
        final ItemNotFoundException exception = assertThrows(ItemNotFoundException.class,
                () -> bookingService.create(bookingInputDto2, user1.getId()));
        assertEquals("Владелец вещи не может забронировать собственную вещь", exception.getMessage());
    }

    @Test
    public void createWithItemNotAvailable() {
        item1.setAvailable(false);
        itemDto1.setAvailable(false);
        itemRepository.save(item1);
        itemService.createItem(itemDto1, user1.getId());
        BookingInputDto bookingInputDto2 = new BookingInputDto(2L,
                item1.getId(), LocalDateTime.now().plusDays(25), LocalDateTime.now().plusDays(35));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.create(bookingInputDto2, user2.getId()));
        assertEquals(("вещь с id " + itemDto1.getId() + " не доступна для бронирования"), exception.getMessage());
    }

    @Test
    public void getAllByBooker() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.ALL, 1, 10);
        assertEquals(allByBooker.size(), 1);
    }

    @Test
    public void getAllByBookerWithWrongPage() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.getAllByBooker(user2.getId(), BookingState.ALL, -1, -10));

        assertEquals("не верно указан количество позиций на странице", exception.getMessage());
    }

    @Test
    public void getAllByBookerCurrent() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.CURRENT, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getAllByBookerPast() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.PAST, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getAllByBookerFuture() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.FUTURE, 1, 10);
        assertEquals(allByBooker.size(), 1);
    }

    @Test
    public void getAllByBookerWaiting() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.WAITING, 1, 10);
        assertEquals(allByBooker.size(), 1);
    }

    @Test
    public void getAllByBookerRejected() {
        List<BookingDto> allByBooker = bookingService.getAllByBooker(user2.getId(), BookingState.REJECTED, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getAllByOwner() {
        List<BookingDto> allByBooker = bookingService.getAllByOwner(user1.getId(), BookingState.ALL, 1, 10);
        assertEquals(allByBooker.size(), 1);
    }

    @Test
    public void getAllByOwnerWrongPage() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.getAllByOwner(user1.getId(), BookingState.ALL, -1, 10));
        assertEquals("не верно указан количество позиций на странице", exception.getMessage());
    }

    @Test
    public void getAllByOwnerCurrent() {
        List<BookingDto> allByBooker = bookingService.getAllByOwner(user1.getId(), BookingState.CURRENT, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getAllByOwnerPast() {
        List<BookingDto> allByBooker = bookingService.getAllByOwner(user1.getId(), BookingState.PAST, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getAllByOwnerWaiting() {
        List<BookingDto> allByBooker = bookingService.getAllByOwner(user1.getId(), BookingState.WAITING, 1, 10);
        assertEquals(allByBooker.size(), 1);
    }

    @Test
    public void getAllByOwnerReject() {
        List<BookingDto> allByBooker = bookingService.getAllByOwner(user1.getId(), BookingState.REJECTED, 1, 10);
        assertEquals(allByBooker.size(), 0);
    }

    @Test
    public void getById() {
        BookingDto bookingDtoGetById = bookingService.getById(bookingInputDto.getId(), user2.getId());
        assertEquals(bookingDtoGetById.getId(), bookingInputDto.getId());
    }

    @Test
    public void update() {
        BookingDto bookingDtoUpdate = bookingService.update(bookingDtoNew.getId(), user1.getId(), true);
        assertEquals(bookingDtoUpdate.getStatus(), Status.APPROVED);
    }
}