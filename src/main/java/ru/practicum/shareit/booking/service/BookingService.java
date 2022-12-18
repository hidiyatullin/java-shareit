package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingInputDto bookingInputDto, Long bookerId);

    List<BookingDto> getAllByBooker(Long userId, BookingState state);

    List<BookingDto> getAllByOwner(Long userId, BookingState state);

    BookingDto getById(Long id, Long userId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    void delete(Long id);
}
