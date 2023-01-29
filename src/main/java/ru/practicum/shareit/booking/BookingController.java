package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid
                             @RequestBody BookingInputDto bookingInputDto,
                             @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return bookingService.create(bookingInputDto, bookerId);
    }

    @GetMapping
    public List<BookingDto> getAllforBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(name = "state", defaultValue = "ALL") String state,
                                            @RequestParam(name = "from", defaultValue = "0") int from,
                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Запрос на получить все бронирования для арендатора создан");
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + state);
        }
        log.info("Пользователь " + userId + " " + bookingState);
        log.info("Список " + bookingService.getAllByBooker(userId, bookingState, from, size));
        return bookingService.getAllByBooker(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "state", defaultValue = "ALL") String state,
                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: " + state));
        log.info("Запрос на получить все бронирования для арендодателя создан");
        return bookingService.getAllByOwner(userId, bookingState, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam(defaultValue = "false") Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId) {
        bookingService.delete(bookingId);
    }
}
