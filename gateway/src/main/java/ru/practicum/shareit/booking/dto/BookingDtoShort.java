package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDtoShort {
    @NotNull
    @FutureOrPresent(message = "Дата начала аренды должна быть текущей или в будущем")
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent(message = "Дата окончания аренды должна быть в будущем")
    private LocalDateTime end;
    @PositiveOrZero(message = "itemId не может быть меньше ноля")
    @NotNull
    private Long itemId;
}