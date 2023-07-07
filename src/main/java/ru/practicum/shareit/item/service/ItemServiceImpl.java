package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public Collection<ItemDto> getAllItemOfUser(long userId) {
        userStorage.isExist(userId);
        return itemStorage.findAllByOwner(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public ItemDto getItemById(long userId, long itemId) {
        userStorage.isExist(userId);
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещи с id = " + itemId + " не существует"));
        return ItemMapper.toItemDto(item);
    }

    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItem(ItemDto itemDto, long userId) {
        userStorage.isExist(userId);
        Item item = itemStorage.save(ItemMapper.toItem(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        userStorage.isExist(userId);
        Item updateItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещи с id = " + itemId + " не существует"));
        Optional.ofNullable(itemDto.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updateItem::setAvailable);
        itemStorage.save(updateItem);
        return ItemMapper.toItemDto(updateItem);
    }
}
