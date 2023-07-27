package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestUtils;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoItemJsonTest {
    @Autowired
    private JacksonTester<BookingDtoItem> json;

    @Test
    void testBookingDtoItem() throws IOException {
        BookingDtoItem bookingDtoItem = TestUtils.bookingDtoItem;
        JsonContent<BookingDtoItem> result = json.write(bookingDtoItem);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(0);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-12-12T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-12-20T10:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}
