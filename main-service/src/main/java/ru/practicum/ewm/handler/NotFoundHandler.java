package ru.practicum.ewm.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.not_found.*;
import ru.practicum.ewm.handler.error.ApiError;
import ru.practicum.ewm.util.DateUtils;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public class NotFoundHandler {

    private static final String RESPONSE_STATUS_NAME = HttpStatus.NOT_FOUND.name();
    private static final String REASON = "The required object was not found.";

    @ExceptionHandler({UserNotFoundException.class,
            CategoryNotFoundException.class,
            EventNotFoundException.class,
            RequestNotFoundException.class,
            CompilationNotFoundException.class})
    public ApiError handleNotFoundException(NotFoundException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

}
