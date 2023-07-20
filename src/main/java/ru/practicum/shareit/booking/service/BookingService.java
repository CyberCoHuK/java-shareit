package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.enums.BookingStates;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(BookingDtoShort bookingDtoShort, Long userId);

    BookingDto requestStatusDecision(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingByUser(Long userId, Long bookingId);

    Collection<BookingDto> getAllBookingsByUser(Long userId, BookingStates state);

    Collection<BookingDto> getAllBookingsByOwner(Long ownerId, BookingStates state);
}
