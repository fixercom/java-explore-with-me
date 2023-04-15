package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.enums.EventSortType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicEventFilter {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private DateRange dateRange;
    private Boolean onlyAvailable;
    private EventSortType eventSortType;

}
