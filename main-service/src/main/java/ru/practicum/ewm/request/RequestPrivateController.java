package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId,
                                                 HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        return requestMapper.toParticipationRequestDto(requestService.createRequest(user, event));
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllUserParticipationRequests(@PathVariable Long userId,
                                                                         HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        User user = userService.getUserById(userId);
        return requestMapper.toParticipationRequestDtoList(requestService.getAllUserRequests(user));
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable Long userId,
                                                     @PathVariable Long requestId,
                                                     HttpServletRequest request) {
        log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        return requestMapper.toParticipationRequestDto(requestService.cancelUserRequest(userId, requestId));
    }

}
