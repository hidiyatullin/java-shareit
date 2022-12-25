package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .lastBooking(new ItemDto.BookingDto())
                .nextBooking(new ItemDto.BookingDto())
                .comments(new ArrayList<>())
                .build();
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .request(itemDto.getRequest())
                .build();
    }

//    public static ItemInfoDto toItemInfoDto(Item item, Booking lastBooking,Booking nextBooking) {
//        return new ItemInfoDto(item.getId(), item.getName(),
//                item.getDescription(), item.getAvailable(),
//                new ItemInfoDto.BookingDto(lastBooking.getId(), lastBooking.getStart(),
//                        lastBooking.getEnd(), lastBooking.getBooker().getId()),
//                new ItemInfoDto.BookingDto(nextBooking.getId(), nextBooking.getStart(),
//                        nextBooking.getEnd(), nextBooking.getBooker().getId()));
//    }
}
