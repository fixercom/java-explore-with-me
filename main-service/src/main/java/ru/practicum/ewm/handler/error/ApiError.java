package ru.practicum.ewm.handler.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.ewm.util.DateUtils;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiError {

    private final String status;
    private final String reason;
    private final String message;
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private final LocalDateTime timestamp;

}
