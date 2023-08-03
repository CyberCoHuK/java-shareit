package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ShortRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Validated
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                                @Valid @RequestBody ShortRequestDto shortRequestDto) {
        return itemRequestClient.createRequest(userId, shortRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getListOfUserRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestClient.getListOfUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getListOfRequests(@RequestHeader(USER_ID_HEADER) long userId,
                                                    @RequestParam(defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(defaultValue = "20") @Positive int size) {
        return itemRequestClient.getListOfRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable long requestId) {
        return itemRequestClient.getRequest(userId, requestId);
    }
}
