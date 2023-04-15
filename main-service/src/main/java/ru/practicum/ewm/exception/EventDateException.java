package ru.practicum.ewm.exception;

public class EventDateException extends RuntimeException {
    public EventDateException() {
        super("the start date of the event to be modified must be no earlier than" +
                " an hour from the date of publication");
    }
}
