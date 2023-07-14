package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.BookingStates;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @Valid @RequestBody BookingDtoShort bookingDtoShort) {
        return bookingService.createBooking(bookingDtoShort, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto requestStatusDecision(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId,
                                            @RequestParam Boolean approved) {
        return bookingService.requestStatusDecision(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingByUser(@RequestHeader(USER_ID_HEADER) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingByUser(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") BookingStates state) {
        return bookingService.getAllBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(defaultValue = "ALL") BookingStates state) {
        return bookingService.getAllBookingsByOwner(ownerId, state);
    }
}
