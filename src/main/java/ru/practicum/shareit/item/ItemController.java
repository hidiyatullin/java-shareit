package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemService;


    @PostMapping
    ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                       @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                       @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/search")
    List<ItemDto> findItems(@RequestParam String text) {
        return itemService.findItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Validated({Create.class}) @RequestHeader("X-Sharer-User-Id") Long authorId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(authorId, itemId, commentDto);
    }
}
