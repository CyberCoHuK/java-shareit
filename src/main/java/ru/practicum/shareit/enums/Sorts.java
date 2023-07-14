package ru.practicum.shareit.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;
@Getter
public enum Sorts {
    START(Sort.by("start").descending());

    private final Sort sort;
    Sorts(Sort sort) {
        this.sort = sort;
    }
}
