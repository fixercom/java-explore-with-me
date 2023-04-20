package ru.practicum.ewm.exception;

public class EventNotPossiblePublishException extends RuntimeException {

    public EventNotPossiblePublishException(String stateName) {
        super(String.format("Cannot publish the event because it's not in the right state: %s", stateName));
    }

}
