package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.DateRange;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.PublicEventFilter;
import ru.practicum.ewm.event.enums.EventSortType;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.stats.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> searchEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size,
                                            HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        PublicEventFilter publicEventFilter = PublicEventFilter.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .dateRange(new DateRange(rangeStart, rangeEnd))
                .onlyAvailable(onlyAvailable)
                .eventSortType(EventSortType.fromName(sort))
                .from(from)
                .size(size)
                .build();
        request.setAttribute("app_name","main application");
        statsClient.createHit(request);
        return eventMapper.toEventShortDtoList(eventService.searchEvents(publicEventFilter));
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        request.setAttribute("app_name","main application");
        statsClient.createHit(request);
        return eventMapper.toEventFullDto(eventService.getEventByIdPublic(id));
    }

}
