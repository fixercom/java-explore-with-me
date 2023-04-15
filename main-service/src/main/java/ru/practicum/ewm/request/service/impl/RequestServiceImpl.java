package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotPossibleCreateRequestException;
import ru.practicum.ewm.exception.not_found.EventNotFoundException;
import ru.practicum.ewm.exception.not_found.RequestNotFoundException;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.DateUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequest createRequest(User requester, Event event) {
        throwExceptionIfRequesterIsInitiator(requester, event);
        throwExceptionIfEventIsNotPublished(event);
        throwExceptionIfRequestLimitIsReached(event);
        RequestStatus status = selectStatusAccordingToModerationRule(event);
        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .requester(requester)
                .event(event)
                .created(DateUtils.now())
                .status(status)
                .build();
        requestRepository.save(participationRequest);
        changeNumberConfirmedEventRequests(event, event.getConfirmedRequests() + 1);
        log.debug("Participation request saved in the database, generated id={}", participationRequest.getId());
        return participationRequest;
    }

    private void changeNumberConfirmedEventRequests(Event event, Integer quantity) {
        event.setConfirmedRequests(quantity);
        eventRepository.save(event);
        log.debug("Confirmed event requests increased by one: {}", event);
    }

    @Override
    public List<ParticipationRequest> getAllRequestsForEvent(Long userId, Event event) {
        throwExceptionIfUserIsNotInitiator(userId, event);
        return requestRepository.findAllByEventId(event.getId());
    }

    @Override
    @Transactional
    public Map<RequestStatus, List<ParticipationRequest>> updateRequestStatuses(Long userId, Event event,
                                                                                List<Long> requestIds,
                                                                                RequestStatus status) {
        throwExceptionIfUserIsNotInitiator(userId, event);
        Integer numberOfConfirmedRequests = event.getConfirmedRequests();
        Integer participantLimit = event.getParticipantLimit();
        List<ParticipationRequest> requests = requestRepository
                .findAllByEventIdAndStatusAndIdIn(event.getId(), RequestStatus.PENDING, requestIds);
        for (ParticipationRequest request : requests) {
            if (numberOfConfirmedRequests < participantLimit) {
                request.setStatus(status);
                numberOfConfirmedRequests++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }
        requestRepository.saveAll(requests);
        changeNumberConfirmedEventRequests(event, numberOfConfirmedRequests);
        return requests.stream().collect(Collectors.groupingBy(ParticipationRequest::getStatus));
    }

    @Override
    public List<ParticipationRequest> getAllUserRequests(User requester) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(requester.getId());
        log.debug("All user requests have been received: {}", requests);
        return requests;
    }

    @Override
    @Transactional
    public ParticipationRequest cancelUserRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        log.debug("Request with id={} was updated in the database: {}", requestId, request);
        return request;
    }

    private void throwExceptionIfRequesterIsInitiator(User requester, Event event) {
        if (event.getInitiator().equals(requester)) {
            throw new NotPossibleCreateRequestException("The initiator of the event cannot" +
                    " add a request to participate in his event");
        }
    }

    private void throwExceptionIfEventIsNotPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotPossibleCreateRequestException("You cannot participate in an unpublished event");
        }
    }

    private void throwExceptionIfRequestLimitIsReached(Event event) {
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new NotPossibleCreateRequestException("The limit of event participants has been reached");
        }
    }

    private void throwExceptionIfUserIsNotInitiator(Long userId, Event event) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException(event.getId());
        }
    }

    private RequestStatus selectStatusAccordingToModerationRule(Event event) {
        return event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED;
    }

}
