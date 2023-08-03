package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.enums.BookingStates;
import ru.practicum.shareit.exceptions.WrongStartEndTimeException;
import ru.practicum.shareit.exceptions.WrongStateException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Validated
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody BookingDtoShort bookingDtoShort) {
        if (bookingDtoShort.getEnd().isBefore(bookingDtoShort.getStart()) ||
                bookingDtoShort.getEnd().isEqual(bookingDtoShort.getStart())) {
            throw new WrongStartEndTimeException(
                    "Дата окончания не может быть раньше или равна дате начала бронирования." +
                            " Невозможно создать бронирование");
        }
        return bookingClient.bookItem(bookingDtoShort, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> requestStatusDecision(@RequestHeader(USER_ID_HEADER) Long userId,
                                                        @PathVariable Long bookingId,
                                                        @RequestParam Boolean approved) {
        return bookingClient.requestStatusDecision(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByUser(@RequestHeader(USER_ID_HEADER) Long userId,
                                                   @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                                       @RequestParam(defaultValue = "20") @Positive int size) {
        BookingStates state = BookingStates.from(stateParam)
                .orElseThrow(() -> new WrongStateException("Unknown state: " + stateParam));
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(defaultValue = "20") @Positive int size) {
        BookingStates state = BookingStates.from(stateParam)
                .orElseThrow(() -> new WrongStateException("Unknown state: " + stateParam));
        return bookingClient.getBookingsByOwner(ownerId, state, from, size);
    }
}