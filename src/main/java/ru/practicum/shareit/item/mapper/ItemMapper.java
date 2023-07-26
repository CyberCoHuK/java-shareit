package ru.practicum.shareit.item.mapper;

import lombok.NonNull;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest() == null ? null : item.getItemRequest().getId())
                .build();
    }

    public static Item toItem(@NonNull ItemDto itemDto, User user) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .build();
    }

    public static ItemDtoResponse toItemDtoWithBooking(Item item, BookingDtoItem next, BookingDtoItem last,
                                                       List<CommentDtoResponse> comments) {
        return ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .nextBooking(next)
                .lastBooking(last)
                .requestId(item.getItemRequest() == null ? null : item.getItemRequest().getId())
                .comments(comments)
                .build();
    }

    public static List<ItemDto> toItemForRequestDto(List<Item> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
