package ru.practicum.ewm.stats.server.hit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.hit.model.Hit;
import ru.practicum.ewm.stats.server.hit.repository.HitRepository;
import ru.practicum.ewm.stats.server.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void createHit(Hit hit) {
        hitRepository.save(hit);
        log.debug("Hit saved in the database, generated id={}", hit.getId());
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> stats;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                stats = hitRepository.findStatsWithUniqueIpInRange(start, end);
                log.debug("Stats with unique IP addresses were obtained from the database {}", stats);
            } else {
                stats = hitRepository.findStatsWithUniqueIpInRange(uris, start, end);
                log.debug("Stats with unique IP addresses were obtained from the database: {}", stats);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                stats = hitRepository.findStatsInRange(start, end);
                log.debug("Stats were obtained from the database: {}", stats);
            } else {
                stats = hitRepository.findStatsInRange(uris, start, end);
                log.debug("Stats were obtained from the database: {}", stats);
            }
        }
        return stats;
    }

}

