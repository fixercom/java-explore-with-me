package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.DateRange;
import ru.practicum.ewm.event.dto.AdminEventFilter;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.enums.AdminActionState;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final EventMapper eventMapper;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                    HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), updateEventAdminRequest);
        AdminActionState adminActionState = AdminActionState.fromName(updateEventAdminRequest.getStateAction());
        Event eventPatch = eventMapper.toEvent(updateEventAdminRequest);
        Long categoryId = updateEventAdminRequest.getCategory();
        Category category = (categoryId != null) ? categoryService.getCategoryById(categoryId) : null;
        eventPatch.setCategory(category);
        return eventMapper.toEventFullDto(eventService.updateEvent(eventId, eventPatch, adminActionState));
    }

    @GetMapping
    public List<EventFullDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<EventState> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size,
                                           HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        AdminEventFilter adminEventFilter = AdminEventFilter.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .dateRange(new DateRange(rangeStart, rangeEnd))
                .build();
        Pageable page = PageRequest.of(from / size, size);
        return eventMapper.toEventFullDtoList(eventService.searchEvents(adminEventFilter, page));
    }
}
