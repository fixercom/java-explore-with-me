package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.enums.EventState;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventFilter {

    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private DateRange dateRange;

}
