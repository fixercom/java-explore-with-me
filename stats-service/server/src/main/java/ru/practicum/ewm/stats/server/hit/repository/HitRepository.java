package ru.practicum.ewm.stats.server.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.ewm.stats.dto.ViewStats(h.app.name, h.uri, COUNT(h.ip))" +
            " FROM Hit h JOIN h.app" +
            " WHERE h.timestamp BETWEEN ?1 AND ?2" +
            " GROUP BY h.app.name, h.uri" +
            " ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> findStatsInRange(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.dto.ViewStats(h.app.name, h.uri, COUNT(h.uri))" +
            " FROM Hit h JOIN h.app" +
            " WHERE h.uri IN ?1 AND h.timestamp BETWEEN ?2 AND ?3" +
            " GROUP BY h.app.name, h.uri" +
            " ORDER BY COUNT(h.uri) DESC")
    List<ViewStats> findStatsInRange(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.dto.ViewStats(h.app.name, h.uri, COUNT(DISTINCT h.ip))" +
            " FROM Hit h JOIN h.app" +
            " WHERE h.timestamp BETWEEN ?1 AND ?2" +
            " GROUP BY h.app.name, h.uri " +
            " ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> findStatsWithUniqueIpInRange(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.dto.ViewStats(h.app.name, h.uri, COUNT(DISTINCT h.ip))" +
            " FROM Hit h JOIN h.app" +
            " WHERE h.uri IN ?1 AND h.timestamp BETWEEN ?2 AND ?3" +
            " GROUP BY h.app.name, h.uri" +
            " ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ViewStats> findStatsWithUniqueIpInRange(List<String> uris, LocalDateTime start, LocalDateTime end);

}
