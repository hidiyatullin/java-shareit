package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
//        return new ItemDto(
//                item.getName(),
//                item.getDescription(),
//                item.isAvailable(),
//                item.getRequest() != null ? item.getRequest().getId() : null
//        );
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(null)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .request(itemDto.getRequest())
                .build();
//        return new Item(
//                itemDto.getName(),
//                itemDto.getDescription(),
//                itemDto.isAvailable(),
//                user,
//                itemDto.getRequest() != null ? itemDto.getRequest().getId() : null
//        );
    }
}
