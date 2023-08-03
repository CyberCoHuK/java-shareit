package ru.practicum.shareit.exceptions;

public class WrongStartEndTimeException extends RuntimeException {
    public WrongStartEndTimeException(String message) {
        super(message);
    }
}