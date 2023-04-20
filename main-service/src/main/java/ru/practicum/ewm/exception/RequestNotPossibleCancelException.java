package ru.practicum.ewm.exception;

public class RequestNotPossibleCancelException extends RuntimeException {

    public RequestNotPossibleCancelException(String message) {
        super(message);
    }

}