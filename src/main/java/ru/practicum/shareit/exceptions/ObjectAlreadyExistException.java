package ru.practicum.shareit.exceptions;

public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException(final String message) {
        super(message);
    }
}
