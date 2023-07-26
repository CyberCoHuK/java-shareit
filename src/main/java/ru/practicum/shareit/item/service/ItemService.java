package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDtoResponse> getAllItemOfUser(long userId, Integer from, Integer size);


    ItemDtoResponse getItemById(long userId, long itemId);


    Collection<ItemDto> searchItem(String text, Integer from, Integer size);


    ItemDto createItem(ItemDto itemDto, long userId);


    ItemDto updateItem(ItemDto itemDto, long userId, long itemId);

    CommentDtoResponse createComment(CommentDto commentDto, long userId, long itemId);
}
