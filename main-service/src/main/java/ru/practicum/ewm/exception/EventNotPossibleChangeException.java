package ru.practicum.ewm.exception;

public class EventNotPossibleChangeException extends RuntimeException {

    public EventNotPossibleChangeException() {
        super("Only pending or canceled events can be changed");
    }

}
