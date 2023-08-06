package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ShortRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                        @RequestBody ShortRequestDto shortRequestDto) {
        return requestService.createRequest(userId, shortRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getListOfUserRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return requestService.getListOfUserRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getListOfRequests(@RequestHeader(USER_ID_HEADER) long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        return requestService.getListOfRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                     @PathVariable long requestId) {
        return requestService.getRequest(userId, requestId);
    }
}
