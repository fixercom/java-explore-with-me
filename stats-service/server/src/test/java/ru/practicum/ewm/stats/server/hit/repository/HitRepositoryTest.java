package ru.practicum.ewm.stats.server.hit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.app.model.App;
import ru.practicum.ewm.stats.server.hit.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HitRepositoryTest {

    private final HitRepository hitRepository;
    private final TestEntityManager entityManager;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    @DisplayName("Getting stats without uri params")
    void findStatsInRangeWhenThereIsNoUris() {
        App app1 = App.builder().name("application 1").build();
        App app2 = App.builder().name("application 2").build();
        entityManager.persist(app1);
        entityManager.persist(app2);
        Hit hit1 = Hit.builder()
                .app(app1)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-04-06 12:00:00", DATE_TIME_FORMATTER))
                .build();
        Hit hit2 = Hit.builder()
                .app(app2)
                .uri("/another")
                .ip("192.165.0.2")
                .timestamp(LocalDateTime.parse("2022-04-02 12:00:00", DATE_TIME_FORMATTER))
                .build();
        entityManager.persist(hit1);
        entityManager.persist(hit2);
        LocalDateTime start = LocalDateTime.parse("2020-04-03 12:00:00", DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse("2025-04-06 23:59:59", DATE_TIME_FORMATTER);
        List<ViewStats> stats = hitRepository.findStatsInRange(start, end);

        assertThat(stats).size().isEqualTo(2);
        assertThat(stats.get(0).getHits()).isEqualTo(1);
        assertThat(stats.get(1).getHits()).isEqualTo(1);
    }

    @Test
    @DisplayName("Getting stats with uri params")
    void findStatsInRange_whenUrisIsPresent() {
        App app = App.builder().name("application").build();
        entityManager.persist(app);
        Hit sameIpHit1 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit otherIpHit = Hit.builder()
                .app(app)
                .uri("/boot")
                .ip("192.165.0.2")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit sameIpHit2 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        entityManager.persist(sameIpHit1);
        entityManager.persist(otherIpHit);
        entityManager.persist(sameIpHit2);
        LocalDateTime start = LocalDateTime.parse("2021-01-01 00:00:01", DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse("2023-01-01 00:00:01", DATE_TIME_FORMATTER);
        List<String> uris = List.of("/test", "/boot");
        List<ViewStats> stats = hitRepository.findStatsInRange(uris, start, end);

        assertThat(stats.size()).isEqualTo(2);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
        assertThat(stats.get(0).getUri()).isEqualTo("/test");
        assertThat(stats.get(1).getHits()).isEqualTo(1);
        assertThat(stats.get(1).getUri()).isEqualTo("/boot");
    }

    @Test
    @DisplayName("Getting stats with unique IP without uri params")
    void findStatsWithUniqueIpInRange_whenThereIsNoUris() {
        App app = App.builder().name("application").build();
        entityManager.persist(app);
        Hit sameIpHit1 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit otherIpHit = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.2")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit sameIpHit2 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        entityManager.persist(sameIpHit1);
        entityManager.persist(otherIpHit);
        entityManager.persist(sameIpHit2);
        LocalDateTime start = LocalDateTime.parse("2021-01-01 00:00:01", DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse("2023-01-01 00:00:01", DATE_TIME_FORMATTER);

        List<ViewStats> stats = hitRepository.findStatsWithUniqueIpInRange(start, end);

        assertThat(stats.size()).isEqualTo(1);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
    }

    @Test
    @DisplayName("Getting stats with unique IP with uri params")
    void findStatsWithUniqueIpInRange_whenUrisIsPresent() {
        App app = App.builder().name("application").build();
        entityManager.persist(app);
        Hit sameIpHit1 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit otherIpHit1 = Hit.builder()
                .app(app)
                .uri("/hello")
                .ip("192.165.0.2")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit sameIpHit2 = Hit.builder()
                .app(app)
                .uri("/test")
                .ip("192.165.0.1")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        Hit otherIpHit2 = Hit.builder()
                .app(app)
                .uri("/check")
                .ip("192.165.0.3")
                .timestamp(LocalDateTime.parse("2022-01-01 00:00:01", DATE_TIME_FORMATTER))
                .build();
        entityManager.persist(sameIpHit1);
        entityManager.persist(otherIpHit1);
        entityManager.persist(otherIpHit2);
        entityManager.persist(sameIpHit2);
        LocalDateTime start = LocalDateTime.parse("2021-01-01 00:00:01", DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse("2023-01-01 00:00:01", DATE_TIME_FORMATTER);
        List<String> uris = List.of("/test", "/check");

        List<ViewStats> stats = hitRepository.findStatsWithUniqueIpInRange(uris, start, end);

        assertThat(stats.size()).isEqualTo(2);
        assertThat(stats.get(0).getHits()).isEqualTo(1);
        assertThat(stats.get(1).getHits()).isEqualTo(1);
    }
}