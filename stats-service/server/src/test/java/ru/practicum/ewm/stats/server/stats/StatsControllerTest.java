package ru.practicum.ewm.stats.server.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.app.service.AppService;
import ru.practicum.ewm.stats.server.hit.mapper.HitMapperImpl;
import ru.practicum.ewm.stats.server.hit.model.Hit;
import ru.practicum.ewm.stats.server.hit.service.HitService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsControllerTest {

    private final ObjectMapper mapper;
    @MockBean
    private final AppService appService;
    @MockBean
    private final HitService hitService;
    @SpyBean
    private final HitMapperImpl hitMapper;
    private final MockMvc mockMvc;

    @Test
    void createHit_whenSuccessful_thenReturnIsCreated() throws Exception {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("app")
                .uri("/event")
                .ip("192.170.1.15")
                .timestamp(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHit))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(appService).findAppByName(anyString());
        verify(appService).createApp(anyString());
        verify(hitMapper).toHit(any(EndpointHit.class));
        verify(hitService).createHit(any(Hit.class));
    }

    @Test
    void getStatsTest_whenSuccessful_thenReturnIsOK() throws Exception {
        List<ViewStats> stats = List.of(
                ViewStats.builder().build(),
                ViewStats.builder().build()
        );
        when(hitService.getStats(any(), any(), any(), any())).thenReturn(stats);

        mockMvc.perform(get("/stats")
                        .param("start", "2023-04-08 14:20:00")
                        .param("end", "2023-04-08 16:20:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));

        verify(hitService).getStats(any(), any(), any(), any());
    }

    @Test
    void getStatsTest_whenStartParamIsAbsent_thenReturnIsBadRequest() throws Exception {
        mockMvc.perform(get("/stats")
                        .param("end", "2023-04-08 16:20:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(hitService, never()).getStats(any(), any(), any(), any());
    }

    @Test
    void getStatsTest_whenEndParamIsAbsent_thenReturnIsBadRequest() throws Exception {
        mockMvc.perform(get("/stats")
                        .param("start", "2023-04-08 14:20:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(hitService, never()).getStats(any(), any(), any(), any());
    }

}