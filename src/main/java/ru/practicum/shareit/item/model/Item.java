package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @PositiveOrZero
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank(message = "Отсутствует название вещи")
    private String name;
    @NotBlank(message = "Отсутствует описание вещи")
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private ItemRequest request;
}
