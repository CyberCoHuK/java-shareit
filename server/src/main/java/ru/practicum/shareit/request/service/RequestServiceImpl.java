package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ShortRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.enums.Sorts.CREATED;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final Sort sort = Sort.by(CREATED.getSort()).descending();

    @Override
    @Transactional
    public ItemRequestDto createRequest(long userId, ShortRequestDto shortRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = RequestMapper.toItemRequest(shortRequestDto, user, now);
        itemRequest = requestRepository.save(itemRequest);
        return RequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getListOfUserRequests(long userId) {
        userRepository.isExist(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequesterId(userId, sort);
        List<Item> items = itemRepository.findAllByItemRequestIn(requests);
        List<ItemRequestDto> answer = new ArrayList<>();
        for (ItemRequest r : requests) {
            answer.add(RequestMapper.toItemRequestDto(r, items.stream()
                    .filter(item -> item.getItemRequest().getId() == r.getId())
                    .collect(Collectors.toList())));
        }
        return answer;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> getListOfRequests(long userId, int from, int size) {
        userRepository.isExist(userId);
        PageRequest page = PageRequest.of(from / size, size, sort);
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNot(userId, page).toList();
        List<Item> items = itemRepository.findAllByItemRequestIn(requests);
        List<ItemRequestDto> answer = new ArrayList<>();
        for (ItemRequest r : requests) {
            answer.add(RequestMapper.toItemRequestDto(r, items.stream()
                    .filter(item -> item.getItemRequest().getId() == r.getId())
                    .collect(Collectors.toList())));
        }
        return answer;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getRequest(long userId, Long requestId) {
        userRepository.isExist(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Запроса с id = " + requestId + " не существует"));
        List<Item> items = itemRepository.findAllByItemRequest(itemRequest);
        return RequestMapper.toItemRequestDto(itemRequest, items);
    }
}
