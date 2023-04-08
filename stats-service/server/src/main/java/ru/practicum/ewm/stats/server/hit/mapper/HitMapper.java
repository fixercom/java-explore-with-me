package ru.practicum.ewm.stats.server.hit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.server.hit.model.Hit;

@Mapper
public interface HitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "app", ignore = true)
    Hit toHit(EndpointHit endpointHit);

}
