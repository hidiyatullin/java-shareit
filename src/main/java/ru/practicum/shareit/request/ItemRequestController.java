package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto,
                                 @Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.create(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("Запрос на получение всех запросов пользователя " + requesterId);
        return itemRequestService.getAllItemRequestsOfUser(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Запрос на получение всех запросов с параметрами from " + from + " size" + size);
        return itemRequestService.getAllItemRequests(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                     @PathVariable Long requestId) {
        return itemRequestService.getByRequestId(requesterId, requestId);
    }
}