package ru.practicum.ewm.request.service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Map;

public interface RequestService {
    ParticipationRequest createRequest(User requester, Event event);

    List<ParticipationRequest> getAllRequestsForEvent(Long userId, Event event);

    Map<RequestStatus, List<ParticipationRequest>> updateRequestStatuses(Long userId, Event event,
                                                                         List<Long> requestIds, RequestStatus status);

    List<ParticipationRequest> getAllUserRequests(User requester);

    ParticipationRequest cancelUserRequest(Long userId, Long requestId);
}
