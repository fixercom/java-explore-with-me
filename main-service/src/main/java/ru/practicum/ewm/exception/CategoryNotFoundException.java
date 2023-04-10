package ru.practicum.ewm.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category", id);
    }
}
