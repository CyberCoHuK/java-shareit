package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {
    public static LocalDateTime now = LocalDateTime.of(2024, 12, 12, 10, 0, 0);
    public static UserDto owner = UserDto.builder()
            .name("owner")
            .email("owner@mail.com")
            .build();
    public static UserDto booker = UserDto.builder()
            .name("booker")
            .email("booker@mail.com")
            .build();
    public static UserDto requester = UserDto.builder()
            .name("requester")
            .email("requestor@mail.com")
            .build();
    public static ItemDto item = ItemDto.builder()
            .name("item")
            .description("description")
            .available(true)
            .requestId(1L)
            .build();
    public static ItemRequestDto request = ItemRequestDto.builder()
            .id(1L)
            .items(List.of(item))
            .description("description")
            .created(LocalDateTime.now())
            .build();
    public static BookingDto booking = BookingDto.builder()
            .id(1L)
            .item(item)
            .start(now)
            .end(now.plusDays(8))
            .booker(booker)
            .status(BookingStatus.WAITING).build();
    public static BookingDtoShort bookingShort = BookingDtoShort.builder()
            .itemId(1L)
            .start(now)
            .end(now.plusDays(8))
            .build();

    public static BookingDtoItem bookingDtoItem = BookingDtoItem.builder()
            .start(now)
            .end(now.plusDays(8))
            .bookerId(1L)
            .item(item)
            .build();
}