package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.BookingStatus;
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
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final RequestRepository requestRepository = mock(RequestRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final ItemService itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
            bookingRepository, requestRepository);
    private static User user;
    private static Item item;
    private static ItemDto itemDto;
    private static ItemDtoResponse itemDtoR;

    @BeforeAll
    static void setUp() {
        user = new User(1, "name", "user1@mail.com");
        item = new Item(1, "item", "best", true, user, null);
        itemDto = ItemMapper.toItemDto(item);
        itemDtoR = ItemMapper.toItemDtoWithBooking(item, null, null, Collections.emptyList());

    }

    @Test
    void getAllItemOfUser() {
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDtoResponse> result = itemService.getAllItemOfUser(1L, 0, 20);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(itemDtoR), result);
        verify(itemRepository, times(1)).findAllByOwnerId(anyLong(), any(Pageable.class));

    }

    @Test
    void getItemById() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        ItemDtoResponse result = itemService.getItemById(1L, 1L);
        assertNotNull(result);
        assertEquals(itemDtoR, result);
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void searchItem() {
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(),
                any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> result = itemService.searchItem("item", 0, 20);
        assertNotNull(result);
        assertEquals(List.of(itemDto), result);
        verify(itemRepository, times(1))
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(),
                        any(Pageable.class));
    }

    @Test
    void createItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        ItemDto result = itemService.createItem(itemDto, 1);
        assertNotNull(result);
        assertEquals(itemDto, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateItem() {
        Item newItem = new Item(2, "item", "best", true, user, null);
        ItemDto itemUpdate = new ItemDto(2, "newName", "newDis", true, null);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(newItem));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(ItemMapper.toItem(itemUpdate, user));
        ItemDto result = itemService.updateItem(itemUpdate, user.getId(), itemUpdate.getId());
        assertNotNull(result);
        assertEquals(itemUpdate, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void createComment() {
        Comment comment = new Comment(1, "text", item, user, null);
        CommentDtoResponse commentDtoR = CommentMapper.toDtoResponse(comment);
        CommentDto commentDto = new CommentDto("text");
        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBefore(anyLong(), anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        CommentDtoResponse result = itemService.createComment(commentDto, user.getId(), item.getId());
        assertNotNull(result);
        commentDtoR.setCreated(result.getCreated());
        assertEquals(commentDtoR, result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}