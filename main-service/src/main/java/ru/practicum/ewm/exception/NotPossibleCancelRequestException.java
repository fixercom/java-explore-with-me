package ru.practicum.ewm.exception;

public class NotPossibleCancelRequestException extends RuntimeException {

    public NotPossibleCancelRequestException(String message) {
        super(message);
    }

}