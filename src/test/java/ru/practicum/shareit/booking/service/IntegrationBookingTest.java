package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.practicum.shareit.TestUtils.*;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationBookingTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    public void getItemByIdTest() {
        UserDto savedUser = userService.createUser(owner);
        UserDto savedBooker = userService.createUser(booker);
        ItemDto savedItem = itemService.createItem(item, savedUser.getId());
        System.out.println(userService.getAllUsers());
        BookingDto bookingDto = bookingService.createBooking(bookingShort, savedBooker.getId());
        BookingDto gotBooking = bookingService.getBookingByUser(savedBooker.getId(), bookingDto.getId());
        assertThat(gotBooking.getId(), notNullValue());
        assertThat(gotBooking.getItem(), equalTo(savedItem));
        assertThat(gotBooking.getBooker(), equalTo(savedBooker));
        assertThat(gotBooking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(gotBooking.getStart(), equalTo(bookingShort.getStart()));
        assertThat(gotBooking.getEnd(), equalTo(bookingShort.getEnd()));
    }
}