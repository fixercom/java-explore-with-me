package ru.practicum.ewm.vote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.vote.mapper.UserVoteMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.vote.dto.UserVoteDto;
import ru.practicum.ewm.vote.service.UserVoteService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users/{userId}/likes/events/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class UserVotePrivateController {

    private final UserVoteService userVoteService;
    private final UserService userService;
    private final EventService eventService;
    private final UserVoteMapper userVoteMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserVoteDto addUserVoteForEvent(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestParam(defaultValue = "true") Boolean isPositive,
                                           HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        return userVoteMapper.toUserVoteDto(userVoteService.addUserVoteForEvent(user, event, isPositive));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserVoteForEvent(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       @RequestParam(defaultValue = "true") Boolean isPositive,
                                       HttpServletRequest request) {
        String params = request.getQueryString();
        if (params != null) {
            log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), params);
        } else {
            log.debug("{} request {} received", request.getMethod(), request.getRequestURI());
        }
        userVoteService.deleteUserVoteForEvent(userId, eventId, isPositive);
    }

}
