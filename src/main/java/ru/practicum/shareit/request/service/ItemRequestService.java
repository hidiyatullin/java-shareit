package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

        ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId);

        List<ItemRequestDto> getAllItemRequestsOfUser(Long requesterId);

        List<ItemRequestDto> getAllItemRequests(Long requesterId, Integer from, Integer size);

        ItemRequestDto getByRequestId(Long requesterId, Long requestId);
}
