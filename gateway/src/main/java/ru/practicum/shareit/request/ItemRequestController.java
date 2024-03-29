package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto,
                                         @Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.create(itemRequestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("Запрос на получение всех запросов пользователя " + requesterId);
        return itemRequestClient.getAllItemRequestsOfUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Запрос на получение всех запросов с параметрами from " + from + " size" + size);
        return itemRequestClient.getAllItemRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                         @PathVariable Long requestId) {
        return itemRequestClient.getByRequestId(requesterId, requestId);
    }
}