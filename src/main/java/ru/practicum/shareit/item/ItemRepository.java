package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface ItemRepository {
    Item save(Item item);

    Item update(Item item, long itemId, long userId);

    Item get(Long itemId);

    List<Item> findByUser(User owner);

    Set<Item> searchItems(String text);
}
