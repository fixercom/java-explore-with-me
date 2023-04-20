package ru.practicum.ewm.exception.not_found;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("User", id);
    }

}
