package ru.practicum.ewm.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.RequestStatus;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;
import java.util.Map;

@Mapper
public interface RequestMapper {

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> participationRequests);

    default EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(
            Map<RequestStatus, List<ParticipationRequest>> map) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(toParticipationRequestDtoList(map.get(RequestStatus.CONFIRMED)))
                .rejectedRequests(toParticipationRequestDtoList(map.get(RequestStatus.REJECTED)))
                .build();
    }

}
