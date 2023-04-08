package ru.practicum.ewm.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String statsServerUrl;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String statsServerUrl) {
        this.statsServerUrl = statsServerUrl;
        restTemplate = new RestTemplateBuilder()
                .requestFactory(SimpleClientHttpRequestFactory::new)
                .build();
    }

    public void createHit(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app((String) request.getAttribute("app_name"))
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        restTemplate.postForEntity(statsServerUrl + "/hit", endpointHit, Void.class);
    }

    public Object[] getStats(String start, String end, List<String> uris, Boolean unique) {
        UriComponents url = UriComponentsBuilder
                .fromUriString(statsServerUrl + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique)
                .build();
        return restTemplate.getForObject(url.toUriString(), Object[].class);
    }

}
