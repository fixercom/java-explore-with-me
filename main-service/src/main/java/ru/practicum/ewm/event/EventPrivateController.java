package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.enums.UserActionState;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final RequestService requestService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto newEventDto,
                                    HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), newEventDto);
        Event event = eventMapper.toEvent(newEventDto);
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        event.setInitiator(user);
        event.setCategory(category);
        return eventMapper.toEventFullDto(eventService.createEvent(event));
    }

    @GetMapping
    public List<EventShortDto> getAllEventsByInitiatorId(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                                         HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        return eventMapper.toEventShortDtoList(eventService.getAllEventsByInitiatorId(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndInitiatorId(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        return eventMapper.toEventFullDto(eventService.getEventByIdAndInitiatorId(eventId, userId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                    HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), updateEventUserRequest);
        UserActionState userActionState = UserActionState.fromName(updateEventUserRequest.getStateAction());
        Event eventPatch = eventMapper.toEvent(updateEventUserRequest);
        Long categoryId = updateEventUserRequest.getCategory();
        Category category = categoryId != null ? categoryService.getCategoryById(categoryId) : null;
        eventPatch.setCategory(category);
        return eventMapper.toEventFullDto(eventService.updateEvent(userId, eventId, eventPatch, userActionState));
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestsForEvent(@PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        Event event = eventService.getEventById(eventId);
        return requestMapper.toParticipationRequestDtoList(requestService.getAllRequestsForEvent(userId, event));
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatuses(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest
                                                                             eventRequestStatusUpdateRequest,
                                                                     HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(),
                request.getRequestURI(), eventRequestStatusUpdateRequest);
        Event event = eventService.getEventById(eventId);
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        RequestStatus status = eventRequestStatusUpdateRequest.getStatus();
        return requestMapper.toEventRequestStatusUpdateResult(requestService
                .updateRequestStatuses(userId, event, requestIds, status));
    }

}
