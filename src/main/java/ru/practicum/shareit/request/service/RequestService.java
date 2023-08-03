package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ShortRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto createRequest(long userId, ShortRequestDto shortRequestDto);

    Collection<ItemRequestDto> getListOfUserRequests(long userId);

    Collection<ItemRequestDto> getListOfRequests(long userId, int from, int size);

    ItemRequestDto getRequest(long userId, Long requestId);
}
