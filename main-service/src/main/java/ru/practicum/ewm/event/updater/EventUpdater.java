package ru.practicum.ewm.event.updater;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.event.model.Event;

@Mapper
public interface EventUpdater {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(Event eventPatch, @MappingTarget Event targetEvent);


}
