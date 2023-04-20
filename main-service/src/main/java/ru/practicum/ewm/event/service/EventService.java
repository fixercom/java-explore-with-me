package ru.practicum.ewm.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.event.dto.AdminEventFilter;
import ru.practicum.ewm.event.dto.PublicEventFilter;
import ru.practicum.ewm.event.enums.AdminActionState;
import ru.practicum.ewm.event.enums.UserActionState;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Long id);

    Event getEventByIdAndInitiatorId(Long eventId, Long initiatorId);

    List<Event> getAllEventsByInitiatorId(Long userId, Integer from, Integer size);

    Event updateEvent(Long userId, Long eventId, Event eventPatch, UserActionState userActionState);

    Event updateEvent(Long eventId, Event eventPatch, AdminActionState adminActionState);

    List<Event> searchEvents(AdminEventFilter adminEventFilter, Pageable pageable);

    List<Event> searchEvents(PublicEventFilter publicEventFilter);

    Event getEventByIdPublic(Long id);

    List<Event> getAllEventsByIdIn(List<Long> eventIds);

}
