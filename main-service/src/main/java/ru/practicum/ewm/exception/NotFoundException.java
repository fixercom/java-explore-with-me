package ru.practicum.ewm.exception;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String objectName, Long id) {
        super(String.format("%s with id=%s was not found", objectName, id));
    }
}
