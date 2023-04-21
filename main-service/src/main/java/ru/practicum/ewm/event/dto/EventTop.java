package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTop {

    private Long id;
    private String title;
    private String annotation;
    private LocalDateTime eventDate;
    private Integer rate;
    private Integer likes;
    private Integer dislikes;

}
