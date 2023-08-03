package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ShortRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
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
import static org.mockito.Mockito.*;

class RequestServiceImplTest {
    private final RequestRepository requestRepository = mock(RequestRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final RequestService requestService = new RequestServiceImpl(userRepository, requestRepository,
            itemRepository);
    private static ItemRequest itemRequest;
    private static ItemRequestDto itemRequestDto;
    private static User user;

    @BeforeAll
    static void setUp() {
        user = new User(1, "name", "user1@mail.com");
        itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        itemRequestDto = RequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(Collections.emptyList());
    }

    @Test
    void createRequest() {
        ShortRequestDto shortRequestDto = new ShortRequestDto("description");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestDto result = requestService.createRequest(1, shortRequestDto);
        assertNotNull(result);
        result.setItems(Collections.emptyList());
        assertEquals(itemRequestDto, result);
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getListOfUserRequests() {
        when(requestRepository.findAllByRequesterId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        Collection<ItemRequestDto> result = requestService.getListOfUserRequests(1);
        assertNotNull(result);
        assertEquals(List.of(itemRequestDto), result);
        verify(requestRepository, times(1)).findAllByRequesterId(anyLong(), any(Sort.class));
        verify(itemRepository, times(1)).findAllByItemRequestIn(anyList());
    }

    @Test
    void getListOfRequests() {
        when(requestRepository.findAllByRequesterIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Collection<ItemRequestDto> result = requestService.getListOfRequests(1, 0, 20);
        assertNotNull(result);
        assertEquals(List.of(itemRequestDto), result);
        verify(requestRepository, times(1)).findAllByRequesterIdNot(anyLong(),
                any(Pageable.class));
        verify(itemRepository, times(1)).findAllByItemRequestIn(anyList());
    }

    @Test
    void getRequest() {
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        ItemRequestDto result = requestService.getRequest(1, 1L);
        assertNotNull(result);
        assertEquals(itemRequestDto, result);
        verify(requestRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findAllByItemRequest(any(ItemRequest.class));
    }
}