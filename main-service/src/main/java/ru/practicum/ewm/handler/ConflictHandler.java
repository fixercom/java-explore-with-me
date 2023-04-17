package ru.practicum.ewm.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.handler.error.ApiError;
import ru.practicum.ewm.util.DateUtils;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice
@ResponseStatus(HttpStatus.CONFLICT)
@Slf4j
public class ConflictHandler {
    private static final String RESPONSE_STATUS_NAME = HttpStatus.CONFLICT.name();
    private static final String RESPONSE_STATUS_NAME_FORBIDDEN = "FORBIDDEN";
    private static final String REASON_DATA_INTEGRITY_VIOLATION_EXCEPTION = "Integrity constraint has been violated.";
    private static final String REASON = "For the requested operation the conditions are not met.";

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

    @ExceptionHandler(EventNotPossibleChangeException.class)
    public ApiError handleEventNotPossibleChangeException(EventNotPossibleChangeException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(EventNotPossibleCancelException.class)
    public ApiError handleEventNotPossibleCancelException(EventNotPossibleCancelException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(EventNotPossiblePublishException.class)
    public ApiError handleEventNotPossiblePublishException(EventNotPossiblePublishException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(EventDateException.class)
    public ApiError handleEventDateException(EventDateException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(RequestNotPossibleCreateException.class)
    public ApiError handleNotPossibleCreateRequestException(RequestNotPossibleCreateException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiError handleNotPossibleCreateRequestException(HttpMessageNotReadableException exception,
                                                            HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(RequestNotPossibleCancelException.class)
    public ApiError handleNotPossibleCreateRequestException(RequestNotPossibleCancelException exception) {
        String message = exception.getMessage();
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME_FORBIDDEN)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

}