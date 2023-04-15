package ru.practicum.ewm.exception.not_found;

public class EventNotFoundException extends NotFoundException {

    public EventNotFoundException(Long id) {
        super("Event", id);
    }

}
