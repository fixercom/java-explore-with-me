package ru.practicum.ewm.exception.not_found;

public class CompilationNotFoundException extends NotFoundException {

    public CompilationNotFoundException(Long id) {
        super("Compilation", id);
    }

}
