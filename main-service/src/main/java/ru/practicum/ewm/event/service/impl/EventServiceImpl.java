package ru.practicum.ewm.event.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.dto.AdminEventFilter;
import ru.practicum.ewm.event.dto.DateRange;
import ru.practicum.ewm.event.dto.PublicEventFilter;
import ru.practicum.ewm.event.enums.AdminActionState;
import ru.practicum.ewm.event.enums.EventSortType;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.enums.UserActionState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.updater.EventUpdater;
import ru.practicum.ewm.exception.EventDateException;
import ru.practicum.ewm.exception.EventNotPossibleCancelException;
import ru.practicum.ewm.exception.EventNotPossibleChangeException;
import ru.practicum.ewm.exception.EventNotPossiblePublishException;
import ru.practicum.ewm.exception.not_found.EventNotFoundException;
import ru.practicum.ewm.util.DateUtils;
import ru.practicum.ewm.util.QPredicates;

import java.util.List;

import static ru.practicum.ewm.event.model.QEvent.event;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventUpdater eventUpdater;

    @Override
    @Transactional
    public Event createEvent(Event event) {
        throwExceptionIfDateIsIncorrect(event, false);
        event.setState(EventState.PENDING);
        event.setCreatedOn(DateUtils.now());
        event.setConfirmedRequests(0);
        eventRepository.save(event);
        log.debug("Event saved in the database, generated id={}", event.getId());
        return event;
    }

    @Override
    public Event getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        log.debug("Event with id={} was obtained from the database: {}", event.getId(), event);
        return event;
    }

    @Override
    public Event getEventByIdAndInitiatorId(Long id, Long initiatorId) {
        Event event = eventRepository.findByIdAndInitiatorId(id, initiatorId)
                .orElseThrow(() -> new EventNotFoundException(id));
        log.debug("Event with id={} was obtained from the database: {}", event.getId(), event);
        return event;
    }

    @Override
    public List<Event> getAllEventsByInitiatorId(Long initiatorId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(initiatorId, page);
        log.debug("Events were obtained from the database, quantity={}", events.size());
        return events;
    }

    @Override
    @Transactional
    public Event updateEvent(Long userId, Long eventId, Event eventPatch, UserActionState userActionState) {
        Event targetEvent = getEventByIdAndInitiatorId(eventId, userId);
        throwExceptionIfEventCannotBeUpdated(targetEvent);
        eventUpdater.update(eventPatch, targetEvent);
        throwExceptionIfDateIsIncorrect(targetEvent, false);
        changeEventStateIfNecessary(targetEvent, userActionState);
        eventRepository.save(targetEvent);
        log.debug("Event with id={} updated in the database: {}", eventId, targetEvent);
        return targetEvent;
    }

    @Override
    @Transactional
    public Event updateEvent(Long eventId, Event eventPatch, AdminActionState adminActionState) {
        Event targetEvent = getEventById(eventId);
        eventUpdater.update(eventPatch, targetEvent);
        throwExceptionIfDateIsIncorrect(targetEvent, true);
        changeEventStateIfNecessary(targetEvent, adminActionState);
        eventRepository.save(targetEvent);
        log.debug("Event with id={} updated in the database: {}", eventId, targetEvent);
        return targetEvent;
    }

    @Override
    public List<Event> searchEvents(AdminEventFilter adminEventFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(adminEventFilter.getUsers(), event.initiator.id::in)
                .add(adminEventFilter.getStates(), event.state::in)
                .add(adminEventFilter.getCategories(), event.category.id::in)
                .add(adminEventFilter.getDateRange(), dr -> generateDateRangeExpression(dr, false))
                .buildAnd();
        List<Event> events = eventRepository.findAll(predicate, pageable).getContent();
        log.debug("Events was obtained from the database: {}", events);
        return events;
    }

    @Override
    public List<Event> searchEvents(PublicEventFilter publicEventFilter) {
        ifTextParamIsBlankThenChangeToNull(publicEventFilter);
        Pageable page = getPageFromPublicEventFilter(publicEventFilter);
        Predicate firstPart = QPredicates.builder()
                .add(publicEventFilter.getText(), event.annotation::containsIgnoreCase)
                .add(publicEventFilter.getText(), event.description::containsIgnoreCase)
                .buildOr();
        Predicate secondPart = QPredicates.builder()
                .add(publicEventFilter.getCategories(), event.category.id::in)
                .add(publicEventFilter.getPaid(), event.paid::eq)
                .add(publicEventFilter.getDateRange(), dr -> generateDateRangeExpression(dr, true))
                .add(publicEventFilter.getOnlyAvailable(), this::generateAvailableExpression)
                .buildAnd();
        Predicate predicate = ExpressionUtils.allOf(firstPart, secondPart);
        List<Event> events = (predicate != null) ?
                eventRepository.findAll(predicate, page).getContent() :
                eventRepository.findAll(page).getContent();
        log.debug("Events was obtained from the database: {}", events);
        return events;
    }

    @Override
    public Event getEventByIdPublic(Long id) {
        Event event = getEventById(id);
        throwExceptionIfEventNotPublished(event);
        return event;
    }

    @Override
    public List<Event> getAllEventsByIdIn(List<Long> eventIds) {
        List<Event> events = eventRepository.findAllByIdIn(eventIds);
        log.debug("Events with ids={} were obtained from the database: {}", eventIds, events);
        return events;
    }

    private Pageable getPageFromPublicEventFilter(PublicEventFilter publicEventFilter) {
        Integer from = publicEventFilter.getFrom();
        Integer size = publicEventFilter.getSize();
        EventSortType sortType = publicEventFilter.getEventSortType();
        Pageable page;
        Sort sort;
        if (sortType != null) {
            sort = (sortType == EventSortType.EVENT_DATE) ?
                    Sort.by("eventDate") : Sort.by("state");
            page = PageRequest.of(from / size, size, sort);
        } else {
            page = PageRequest.of(from / size, size);
        }
        return page;
    }

    private void throwEventNotPossibleCancelOrPublishException(Event event, AdminActionState adminActionState) {
        if (adminActionState == AdminActionState.PUBLISH_EVENT) {
            throw new EventNotPossiblePublishException(event.getState().name());
        } else {
            throw new EventNotPossibleCancelException(event.getState().name());
        }
    }

    private void throwExceptionIfEventCannotBeUpdated(Event event) {
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventNotPossibleChangeException();
        }
    }

    private void throwExceptionIfEventNotPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new EventNotFoundException(event.getId());
        }
    }

    private void ifTextParamIsBlankThenChangeToNull(PublicEventFilter publicEventFilter) {
        if (publicEventFilter.getText() != null && publicEventFilter.getText().isBlank()) {
            publicEventFilter.setText(null);
        }
    }

    private void throwExceptionIfDateIsIncorrect(Event targetEvent, Boolean isAdmin) {
        if (isAdmin) {
            if (targetEvent.getEventDate().isBefore(DateUtils.now().plusHours(1))) {
                throw new EventDateException("The start date of the event to be modified " +
                        "must be no earlier than one hour from the date of publication");
            }
        } else {
            if (targetEvent.getEventDate().isBefore(DateUtils.now().plusHours(2))) {
                throw new EventDateException("The date cannot be earlier than two hours from the current moment");
            }
        }
    }

    private void changeEventStateIfNecessary(Event event, UserActionState userActionState) {
        if (userActionState != null) {
            changeEventState(event, userActionState);
        }
    }

    private void changeEventState(Event event, UserActionState userActionState) {
        if (userActionState == UserActionState.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        } else if (userActionState == UserActionState.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
    }

    private void changeEventStateIfNecessary(Event event, AdminActionState adminActionState) {
        if (adminActionState != null) {
            changeEventState(event, adminActionState);
        }
    }

    private void changeEventState(Event event, AdminActionState adminActionState) {
        if (adminActionState == AdminActionState.PUBLISH_EVENT && event.getState() == EventState.PENDING) {
            event.setState(EventState.PUBLISHED);
        } else if (adminActionState == AdminActionState.REJECT_EVENT && event.getState() != EventState.PUBLISHED) {
            event.setState(EventState.CANCELED);
        } else {
            throwEventNotPossibleCancelOrPublishException(event, adminActionState);
        }
    }

    private BooleanExpression generateDateRangeExpression(DateRange dateRange, Boolean isAdmin) {
        if (dateRange.getRangeStart() != null && dateRange.getRangeEnd() != null) {
            return event.eventDate.between(dateRange.getRangeStart(), dateRange.getRangeEnd());
        } else if (dateRange.getRangeStart() != null) {
            return event.eventDate.after(dateRange.getRangeStart().minusSeconds(1));
        } else if (dateRange.getRangeEnd() != null) {
            return event.eventDate.before(dateRange.getRangeEnd().plusSeconds(1));
        }
        return isAdmin ? event.eventDate.after(DateUtils.now()) : null;
    }

    private BooleanExpression generateAvailableExpression(Boolean available) {
        return available ? event.confirmedRequests.lt(event.participantLimit) : null;
    }

}
