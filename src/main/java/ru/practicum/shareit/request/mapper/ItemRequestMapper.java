package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

        public static ItemRequestDto toDto(ItemRequest itemRequest) {
            return ItemRequestDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requester(itemRequest.getRequester())
                    .created(itemRequest.getCreated())
                    .build();
        }

        public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
            return ItemRequest.builder()
                    .id(itemRequestDto.getId())
                    .description(itemRequestDto.getDescription())
                    .requester(itemRequestDto.getRequester())
                    .created(itemRequestDto.getCreated())
                    .build();
        }

    public static List<ItemRequestDto.ItemAnswerDto> itemRequestDtoWithAnswer(ItemRequestDto itemRequestDto,
                                                                              List<Item> items) {
        List<ItemRequestDto.ItemAnswerDto> newItemList = new ArrayList<>();
        for (Item item : items) {
            newItemList.add(ItemRequestDto.ItemAnswerDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .idOwner(item.getOwner().getId())
                    .requestId(item.getItemRequest().getId())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .build());
        }
        return newItemList;
    }
}
