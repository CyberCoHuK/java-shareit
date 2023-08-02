package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Include
    private long id;
    private String name;
    private String email;
}

