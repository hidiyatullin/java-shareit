package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Exeption.IncorrectUserOfItemException;
import ru.practicum.shareit.Exeption.ItemNotFoundException;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.Exeption.ValidationException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId).get();
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        Item oldItem = itemRepository.findById(itemId).get();
        if (oldItem.getOwner().getId() != userId) {
            throw new IncorrectUserOfItemException("Неверный пользователь");
        }
            if (itemDto.getName() != null) {
                oldItem.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                oldItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                oldItem.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getRequest() != null) {
                oldItem.setRequest(itemDto.getRequest());
            }
            return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    @Override
    public ItemDto getItem(Long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Товар с id " + itemId + " не зарегестрирован"));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        Booking lastBookings = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now());
        Booking nextBookings = bookingRepository.findOneByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        Long ownerId = item.getOwner().getId();
        if (Objects.equals(ownerId, userId)) {
            if (lastBookings != null) {
                itemDto.setLastBooking(new ItemDto.BookingDto(lastBookings.getId(),
                        lastBookings.getStart(), lastBookings.getEnd(),
                        lastBookings.getBooker().getId()));
            }
            if (nextBookings != null) {
                itemDto.setNextBooking(new ItemDto.BookingDto(nextBookings.getId(),
                        nextBookings.getStart(), nextBookings.getEnd(),
                        nextBookings.getBooker().getId()));
            }
        } else {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = CommentMapper.toDto(comment);
            commentDto.setAuthorName(comment.getAuthor().getName());
            commentDto.setIdItem(comment.getItem().getId());
            commentsDto.add(commentDto);
        }
        if (commentsDto == null) {
            itemDto.setComments(null);
        } else {
            itemDto.setComments(commentsDto);
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        List<ItemDto> itemDtos = itemRepository.findAll().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .map(ItemMapper::toItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        List<ItemDto> itemNewDtos = new ArrayList<>();
        for (ItemDto itemDto : itemDtos) {
            Long id = itemDto.getId();
            Booking lastBookings = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(id, LocalDateTime.now());
            Booking nextBookings = bookingRepository.findOneByItemIdAndStartAfterOrderByStartAsc(id, LocalDateTime.now());
            List<Comment> comments = commentRepository.findAllByItemId(id);

            List<CommentDto> commentsDto = new ArrayList<>();
            for (Comment comment : comments) {
                CommentDto commentDto = CommentMapper.toDto(comment);
                commentDto.setAuthorName(comment.getAuthor().getName());
                commentDto.setIdItem(comment.getItem().getId());
                commentsDto.add(commentDto);
            }
            Item item = itemRepository.findById(id).orElseThrow();
            User owner = item.getOwner();
            if (Objects.equals(owner.getId(), userId)) {
                if (lastBookings != null) {
                    itemDto.setLastBooking(new ItemDto.BookingDto(lastBookings.getId(),
                            lastBookings.getStart(), lastBookings.getEnd(),
                            lastBookings.getBooker().getId()));
                } else {
                    itemDto.setLastBooking(null);
                }
                if (nextBookings != null) {
                    itemDto.setNextBooking(new ItemDto.BookingDto(nextBookings.getId(),
                            nextBookings.getStart(), nextBookings.getEnd(),
                            nextBookings.getBooker().getId()));
                } else {
                    itemDto.setNextBooking(null);
                }
            } else {
                itemDto.setLastBooking(null);
                itemDto.setNextBooking(null);
            }
            if (commentsDto == null) {
                itemDto.setComments(null);
            } else {
                itemDto.setComments(commentsDto);
            }
            itemNewDtos.add(itemDto);
        }
        return itemNewDtos;
    }

    @Override
    public List<ItemDto> findItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByDescriptionContainingIgnoreCase(text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long authorId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("нет текста комментария");
        }
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("нет такого пользователя"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("нет такого пользователя"));
        bookingRepository.findAllByBookerIdAndEndBeforeOrderByIdDesc(authorId, LocalDateTime.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ValidationException("Пользователь с id " +
                        authorId + "не бронировал вещь с id " + itemId));
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        CommentDto commentDtoNew = CommentMapper.toDto(commentRepository.save(comment));
        return commentDtoNew;
    }
}
