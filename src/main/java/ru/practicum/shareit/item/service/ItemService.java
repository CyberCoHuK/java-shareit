package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Collection<ItemDto> getAllItemOfUser(long userId) {
        userStorage.isExist(userId);
        return itemStorage.getAllItemOfUser(userId);
    }

    public ItemDto getItemById(long userId, long itemId) {
        userStorage.isExist(userId);
        return itemStorage.getItemById(itemId);
    }

    public Collection<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text);
    }

    public ItemDto createItem(ItemDto itemDto, long userId) {
        userStorage.isExist(userId);
        return itemStorage.createItem(itemDto, userId);
    }

    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        userStorage.isExist(userId);
        return itemStorage.updateItem(itemDto, itemId, userId);
    }
}
