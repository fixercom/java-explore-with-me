package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

@Mapper
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "locationLatitude", source = "location.lat")
    @Mapping(target = "locationLongitude", source = "location.lon")
    Event toEvent(NewEventDto newEventDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "locationLatitude", source = "location.lat")
    @Mapping(target = "locationLongitude", source = "location.lon")
    Event toEvent(UpdateEventUserRequest updateEventUserRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "locationLatitude", source = "location.lat")
    @Mapping(target = "locationLongitude", source = "location.lon")
    Event toEvent(UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(target = "location",
            expression = "java(new Location(event.getLocationLatitude(), event.getLocationLongitude()))")
    EventFullDto toEventFullDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> events);

    List<EventFullDto> toEventFullDtoList(List<Event> events);

}
