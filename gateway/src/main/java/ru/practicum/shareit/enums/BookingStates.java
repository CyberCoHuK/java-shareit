package ru.practicum.shareit.enums;

import java.util.Optional;

public enum BookingStates {

    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static Optional<BookingStates> from(String stringState) {
        for (BookingStates state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}