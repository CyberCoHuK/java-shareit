package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemInMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Collection<ItemDto> getAllItemOfUser(long userId) {
        log.info("Вещи пользователя с индентификатором {} возвращены", userId);
        return items.values()
                .stream()
                .filter(item -> item.getOwner() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        isExist(itemId);
        log.info("Вещь с индентификатором {} возвращена", itemId);
        return ItemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public Collection<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            log.info("Запрос поиска пуст. Возвращен пустой список");
            return Collections.emptyList();
        }
        log.info("Возвращен список вещей по запросу: {}", text);
        return items.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> StringUtils.containsIgnoreCase(item.getName(), text)
                        || StringUtils.containsIgnoreCase(item.getDescription(), text))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        Item newItem = ItemMapper.toItem(itemDto, itemId++, userId);
        items.put(newItem.getId(), newItem);
        log.info("Создана новая вещь с индентификатором {}", newItem.getId());
        return ItemMapper.toItemDto(items.get(newItem.getId()));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        isExist(itemId);
        haveUserItem(itemId, userId);
        Item updateItem = items.get(itemId);
        Optional.ofNullable(itemDto.getName()).ifPresent(updateItem::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(updateItem::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(updateItem::setAvailable);
        items.replace(itemId, updateItem);
        log.info("Обновлена вещь с индентификатором {}", itemId);
        return ItemMapper.toItemDto(items.get(itemId));
    }

    private void isExist(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ObjectNotFoundException("Вещи с таким индентификатором " + itemId + " не существует");
        }
    }

    private void haveUserItem(long itemId, long userId) {
        if (items.get(itemId).getOwner() != userId) {
            throw new ObjectNotFoundException("Вещь не найдена у пользователя с индентификатором " + userId);
        }
    }
}
