package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDtoResponse> getAllItemOfUser(@RequestHeader(USER_ID_HEADER) long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        return itemService.getAllItemOfUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getItemById(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "20") int size) {
        return itemService.searchItem(text, from, size);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID_HEADER) long userId,
                              @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) long userId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestHeader(USER_ID_HEADER) long userId,
                                            @PathVariable long itemId,
                                            @RequestBody CommentDto commentDto) {
        return itemService.createComment(commentDto, userId, itemId);
    }
}
