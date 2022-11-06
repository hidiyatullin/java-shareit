package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.getUser(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        User owner = userRepository.getUser(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.update(item, itemId, userId));
    }

    @Override
    public ItemDto getItem(Long itemId, long userId) {
        return ItemMapper.toItemDto(itemRepository.get(itemId));
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        User owner = userRepository.getUser(userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemRepository.findByUser(owner)) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> findItems(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        Set<Item> items = itemRepository.findItems(text);
        for(Item item : items) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
