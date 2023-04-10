package ru.practicum.ewm.stats.server.hit.service;

import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    void createHit(Hit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
