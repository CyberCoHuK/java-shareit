package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemStorage {
    Collection<ItemDto> getAllItemOfUser(long userId);

    ItemDto getItemById(long itemId);

    Collection<ItemDto> searchItem(String text);

    ItemDto createItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);
}
