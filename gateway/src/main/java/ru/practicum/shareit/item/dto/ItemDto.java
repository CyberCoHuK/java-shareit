package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    @PositiveOrZero
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank(message = "Отсутствует название вещи")
    private String name;
    @NotBlank(message = "Отсутствует описание вещи")
    private String description;
    @NotNull
    @AssertTrue
    private Boolean available;
    private Long requestId;
}