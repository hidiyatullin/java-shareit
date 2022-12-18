package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    CommentDto addComment(Long authorId, Long itemId, CommentDto commentDto);

    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItem(Long itemId, long userId);

    List<ItemDto> getItems(long userId);

    List<ItemDto> findItems(String text);
}
