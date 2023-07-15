package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.enums.BookingStatus.APPROVED;
import static ru.practicum.shareit.enums.Sorts.START;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final Comparator<BookingDtoItem> BOOKING_COMPARATOR = Comparator.comparing(BookingDtoItem::getStart);
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final Sort sort = Sort.by(START.getSort()).descending();

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDtoResponse> getAllItemOfUser(long userId) {
        userRepository.isExist(userId);
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        List<ItemDtoResponse> dtoItems = new ArrayList<>();
        for (Item it : items) {
            dtoItems.add(getItemDtoWithBooking(it));
        }
        return dtoItems;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDtoResponse getItemById(long userId, long itemId) {
        userRepository.isExist(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещи с id = " + itemId + " не существует"));
        ItemDtoResponse itemDto = getItemDtoWithBooking(item);
        if (item.getOwner().getId() != userId) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (!StringUtils.hasLength(text)) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private ItemDtoResponse getItemDtoWithBooking(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Collection<BookingDtoItem> bookings = bookingRepository.findAllByItem(item, sort)
                .stream().map(BookingMapper::toBookingDtoItem).collect(Collectors.toList());
        BookingDtoItem last = bookings.stream()
                .sorted(BOOKING_COMPARATOR)
                .filter(b -> b.getStart().isBefore(now))
                .reduce((first, second) -> second).stream()
                .findFirst()
                .orElse(null);
        BookingDtoItem next = bookings.stream()
                .sorted(BOOKING_COMPARATOR)
                .filter(b -> b.getStart().isAfter(now) && b.getStatus().equals(APPROVED))
                .findFirst()
                .orElse(null);
        List<CommentDtoResponse> comments = commentRepository.findAllByItemId(item.getId()).stream()
                .map(CommentMapper::toDtoResponse)
                .collect(Collectors.toList());
        return ItemMapper.toItemDtoWithBooking(item, next, last, comments);
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Item item = itemRepository.save(ItemMapper.toItem(itemDto, user));
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        userRepository.isExist(userId);
        Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещи с id = " + itemId + " не существует"));
        Optional.ofNullable(itemDto.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updateItem::setAvailable);
        itemRepository.save(updateItem);
        return ItemMapper.toItemDto(updateItem);
    }

    @Transactional
    @Override
    public CommentDtoResponse createComment(CommentDto commentDto, long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещи с id = " + itemId + " не существует"));
        Collection<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBefore(itemId,
                userId, APPROVED, LocalDateTime.now());
        if (bookings == null || bookings.isEmpty()) {
            throw new BadRequestException("Бронирований не найдено");
        }
        Comment comment = CommentMapper.toComment(commentDto, user, item, LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.toDtoResponse(comment);
    }
}
