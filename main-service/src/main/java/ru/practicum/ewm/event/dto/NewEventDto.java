package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.DateUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @Size(min = 20, max = 2000)
    @NotBlank(message = "annotation must not be blank")
    private String annotation;
    @NotNull(message = "category must not be null")
    private Long category;
    @Size(min = 20, max = 7000)
    @NotBlank(message = "description must not be blank")
    private String description;
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @NotNull(message = "location must not be null")
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @Size(min = 3, max = 120)
    @NotBlank(message = "annotation must not be blank")
    private String title;

}
