package ru.practicum.ewm.exception.not_found;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException(Long id) {
        super("Category", id);
    }

}
