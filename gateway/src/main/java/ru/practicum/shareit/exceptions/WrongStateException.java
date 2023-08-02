package ru.practicum.shareit.exceptions;

public class WrongStateException extends RuntimeException {
    public WrongStateException(String message) {
        super(message);
    }
}
