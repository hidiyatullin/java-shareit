package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.*;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(BookingInputDto bookingInputDto, Long bookerId) {
        User userBooker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователя с id " + bookerId + " не зарегестрирован"));
        validBookingDate(bookingInputDto);
//        if (bookingInputDto.getItemId() == null) {
//            throw new ItemNotFoundException("Укажите предмет для бронирования");
//        }
        Long itemId = bookingInputDto.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь с id " + itemId + " не найдена"));
        User ownerUser = item.getOwner();
        if (ownerUser.equals(userBooker)) {
            throw new ItemNotFoundException("Владелец вещи не может забронировать собственную вещь");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("вещь с id " + itemId + " не доступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingInputDto);
        booking.setBooker(userBooker);
        booking.setItem(item);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public List<BookingDto> getAllByBooker(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет такого пользователя"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerCurrent(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByIdDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdState(userId, Status.WAITING.toString());
                log.info("ServiceImpl " + bookings);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdState(userId, Status.REJECTED.toString());
                break;
        }
        if (bookings != null) {
            return bookings
                    .stream()
                    .map(BookingMapper::toDto)
                    .collect(Collectors.toList());
        }
        throw new ItemNotFoundException("В базе нет такого объекта");
    }

    @Override
    public List<BookingDto> getAllByOwner(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет такого пользователя"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdCurrent(userId, LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdStatePast(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdStateFuture(userId, LocalDateTime.now());
                System.out.println(bookings.size());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdState(userId, Status.WAITING.toString());
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdState(userId, Status.REJECTED.toString());
                break;
        }
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getById(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("нет такого объекта"));
        User bookerUser = booking.getBooker();
        User ownerUser = booking.getItem().getOwner();
        if (!bookerUser.getId().equals(userId) && !ownerUser.getId().equals(userId)) {
            throw new BookingNotFoundException("у пользователя нет такого бронирования");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("нет такого объекта"));
        return BookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("нет такого объекта"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("нет такого объекта"));
        User bookerUser = booking.getBooker();
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("нет такого объекта"));
        User ownerUser = item.getOwner();
        if (!Objects.equals(bookerUser.getId(), userId)) {
            if (Objects.equals(ownerUser.getId(), userId)) {
                if (booking.getStatus() == Status.APPROVED) {
                    throw new ValidationException("Статус APPROVED уже установлен");
                }
                booking.setIsApproved(approved);
                if (booking.getIsApproved()) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
                }
            } else {
                throw new ValidationException("Изменения может вносить только пользователь");
            }
        } else {
            throw new IncorrectUserOfItemException("Арендатор не может устанавливать статус");
        }
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование с id " + id + " не зарегестрирован"));
        bookingRepository.delete(booking);
    }

    private void validBookingDate(BookingInputDto bookingInputDto) {
        LocalDateTime start = bookingInputDto.getStart();
        LocalDateTime end = bookingInputDto.getEnd();
        if (start.isAfter(end) || end.isBefore(start)) {
            throw new ValidationException("Дата начала бронирования должна быть раньше даты окончания бронирования");
        }
    }
}