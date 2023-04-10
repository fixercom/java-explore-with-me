package ru.practicum.ewm.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.handler.error.ApiError;
import ru.practicum.ewm.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Slf4j
public class BadRequestHandler {

    private static final String RESPONSE_STATUS_NAME = HttpStatus.BAD_REQUEST.name();
    private static final String REASON = "Incorrectly made request.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), exception.getTarget());
        String fieldName = Objects.requireNonNull(exception.getFieldError()).getField();
        Object rejectedValue = Objects.requireNonNull(exception.getFieldError()).getRejectedValue();
        String reasonForRejection = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        String message = String.format("Field: %s. Error: %s. Value: %s", fieldName, reasonForRejection, rejectedValue);
        log.warn("{}: {}", exception.getClass().getSimpleName(), message);
        return ApiError.builder()
                .status(RESPONSE_STATUS_NAME)
                .reason(REASON)
                .message(message)
                .timestamp(DateUtils.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                              HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
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
