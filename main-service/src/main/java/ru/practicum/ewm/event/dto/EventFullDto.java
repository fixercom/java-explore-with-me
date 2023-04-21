package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.util.DateUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;

}
