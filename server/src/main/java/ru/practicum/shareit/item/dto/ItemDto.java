package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
