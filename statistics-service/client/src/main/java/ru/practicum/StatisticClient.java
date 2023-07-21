package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.exception.StatisticWrongTimeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${service.uri}")
    private String serverUri;

    @Autowired
    private final WebClient webClient;


    public ResponseEntity<Object> createRegistry(RegistryRequest registryRequest) {

        return webClient.post()
                .uri(serverUri + "/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(registryRequest)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    public ResponseEntity<Object> getStats(String start, String end, String urisString, boolean unique) {
        checkTime(start, end);

        String uri;

        if (urisString.isEmpty()) {
            uri = UriComponentsBuilder.fromHttpUrl(serverUri + "/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("unique", unique)
                    .build()
                    .toUriString();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(serverUri + "/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("uris", urisString)
                    .queryParam("unique", unique)
                    .build()
                    .toUriString();
        }

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    private void checkTime(String startString, String endString) {
        LocalDateTime start = LocalDateTime.parse(startString, formatter);
        LocalDateTime end = LocalDateTime.parse(endString, formatter);

        if (start.isAfter(end)) {
            log.error("Время окончания периода не может быть раньше времени начала периода");
            throw new StatisticWrongTimeException("Время окончания периода " +
                    "не может быть раньше времени начала периода");
        }
        if (start.equals(end)) {
            log.error("Время начала и окончания периода не может быть равно");
            throw new StatisticWrongTimeException("Время начала и окончания периода не может быть равно");
        }
    }
}
