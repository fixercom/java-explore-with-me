package ru.practicum.ewm.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("User", id);
    }
}
