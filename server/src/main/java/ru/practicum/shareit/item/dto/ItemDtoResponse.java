package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoResponse {

    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private Long requestId;
    private List<CommentDtoResponse> comments;
}
