package ru.practicum.ewm.exception;

public class EventNotPossibleCancelException extends RuntimeException {

    public EventNotPossibleCancelException(String stateName) {
        super(String.format("Cannot cancel the event because it's not in the right state: %s", stateName));
    }

}
