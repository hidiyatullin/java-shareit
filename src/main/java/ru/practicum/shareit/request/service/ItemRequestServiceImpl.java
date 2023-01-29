package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.ItemNotFoundException;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId) {
        User user = validateUser(requesterId);
        ItemRequest newItemRequest = new ItemRequest(null, itemRequestDto.getDescription(), user, LocalDateTime.now());
        return ItemRequestMapper.toDto(itemRequestRepository.save(newItemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsOfUser(Long requesterId) {
        validateUser(requesterId);
        List<ItemRequest> requests = itemRequestRepository.findByUserId(requesterId);
        List<ItemRequestDto> requestDtos = requests.stream()
                .map(ItemRequestMapper::toDto).collect(Collectors.toList());

        List<Long> ids = requestDtos.stream()
                .map(ItemRequestDto::getId)
                .filter(id -> id != 0)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findByRequest_IdIn(ids);
        List<ItemRequestDto> requestDtosWithAnswer = new ArrayList<>();
        for (ItemRequestDto requestDto : requestDtos) {
            List<ItemRequestDto.ItemAnswerDto> newItemList =
                    ItemRequestMapper.itemRequestDtoWithAnswer(requestDto, items);
            requestDto.setItems(newItemList);
            requestDtosWithAnswer.add(requestDto);
        }
        return requestDtosWithAnswer;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long requesterId, Integer from, Integer size) {
        validateUser(requesterId);
        if (from < 0 || size <= 0) {
            throw new ValidationException("не верно указан количество позиций на странице");
        }
        int page = from / size;
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        final PageRequest pageRequest = PageRequest.of(page, size, sortByCreated);
        List<ItemRequest> itemRequests = new ArrayList<>(itemRequestRepository.findAllByUserID(requesterId, pageRequest));
        List<ItemRequestDto> requestDtos = itemRequests.stream()
                .map(ItemRequestMapper::toDto).collect(Collectors.toList());
        List<ItemRequestDto> requestDtosWithAnswer = new ArrayList<>();
        for (ItemRequestDto requestDto : requestDtos) {
            Long idRequestDto = requestDto.getId();
            if (idRequestDto != 0) {
                List<Item> items = itemRepository.findByIdRequest(idRequestDto);
                List<ItemRequestDto.ItemAnswerDto> newItemList =
                        ItemRequestMapper.itemRequestDtoWithAnswer(requestDto, items);
                requestDto.setItems(newItemList);
                requestDtosWithAnswer.add(requestDto);
            }
        }
        return requestDtosWithAnswer;
    }

    @Override
    public ItemRequestDto getByRequestId(Long requesterId, Long id) {
        validateUser(requesterId);
        ItemRequest request = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Запрос с id " + id + " не найден"));
        ItemRequestDto requestDto = ItemRequestMapper.toDto(request);
        List<Item> items = itemRepository.findByIdRequest(requestDto.getId());
        List<ItemRequestDto.ItemAnswerDto> newItemList =
                ItemRequestMapper.itemRequestDtoWithAnswer(requestDto, items);
        requestDto.setItems(newItemList);
        return requestDto;
    }

    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Нет такого пользователя"));
        return user;
    }
}