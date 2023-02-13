package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.Update;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItems(@RequestParam String text,
                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemClient.findItems(text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long authorId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemClient.addComment(authorId, itemId, commentDto);
    }
}
