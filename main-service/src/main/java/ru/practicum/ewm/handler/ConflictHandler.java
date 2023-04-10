package ru.practicum.ewm.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.handler.error.ApiError;
import ru.practicum.ewm.util.DateUtils;


@RestControllerAdvice
@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class ConflictHandler {
    private static final String RESPONSE_STATUS_NAME = HttpStatus.CONFLICT.name();
    private static final String REASON_DATA_INTEGRITY_VIOLATION_EXCEPTION = "Integrity constraint has been violated.";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME)
                .reason(REASON_DATA_INTEGRITY_VIOLATION_EXCEPTION)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }
}
