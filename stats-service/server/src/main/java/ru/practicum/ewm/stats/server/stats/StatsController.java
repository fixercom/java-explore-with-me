package ru.practicum.ewm.stats.server.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHit;
import ru.practicum.ewm.stats.dto.ViewStats;
import ru.practicum.ewm.stats.server.app.model.App;
import ru.practicum.ewm.stats.server.app.service.AppService;
import ru.practicum.ewm.stats.server.hit.mapper.HitMapper;
import ru.practicum.ewm.stats.server.hit.model.Hit;
import ru.practicum.ewm.stats.server.hit.service.HitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final AppService appService;
    private final HitService hitService;
    private final HitMapper hitMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody @Valid EndpointHit endpointHit, HttpServletRequest request) {
        log.debug("{} request {} received: {}", request.getMethod(), request.getRequestURI(), endpointHit);
        String appName = endpointHit.getApp();
        App app = appService.findAppByName(appName).orElseGet(() -> appService.createApp(appName));
        Hit hit = hitMapper.toHit(endpointHit);
        hit.setApp(app);
        hitService.createHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam LocalDateTime start,
                                    @RequestParam LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique,
                                    HttpServletRequest request) {
        log.debug("{} request {}?{} received", request.getMethod(), request.getRequestURI(), request.getQueryString());
        return hitService.getStats(start, end, uris, unique);
    }

}
