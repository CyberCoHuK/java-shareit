package ru.practicum.shareit.enums;

import lombok.Getter;

@Getter
public enum Sorts {
    START("start");

    private final String sort;

    Sorts(String sort) {
        this.sort = sort;
    }
}
