package ru.practicum.ewm.exception.not_found;

public class RequestNotFoundException extends NotFoundException {

    public RequestNotFoundException(Long id) {
        super("Request", id);
    }

}
