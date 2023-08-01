package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.client.dto.RegistryRequest;
import ru.practicum.client.dto.RegistryResponse;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticWebClient {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${statistic.uri}")
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

    public List<RegistryResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> urisList, boolean unique) {
        //checkTime(start, end);

        String uri;

        if (urisList.isEmpty()) {
            uri = UriComponentsBuilder.fromHttpUrl(serverUri + "/stats")
                    .queryParam("start", start.format(formatter))
                    .queryParam("end", end.format(formatter))
                    .queryParam("unique", unique)
                    .build()
                    .toUriString();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(serverUri + "/stats")
                    .queryParam("start", start.format(formatter))
                    .queryParam("end", end.format(formatter))
                    .queryParam("uris", urisList)
                    .queryParam("unique", unique)
                    .build()
                    .toUriString();
        }

        return Objects.requireNonNull(webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(RegistryResponse.class)
                .block()).getBody();
    }

//    private void checkTime(LocalDateTime start, LocalDateTime end) {
//
//        if (start.isAfter(end)) {
//            log.error("Время окончания периода не может быть раньше времени начала периода");
//            throw new StatisticWrongTimeException("Время окончания периода " +
//                    "не может быть раньше времени начала периода");
//        }
//        if (start.equals(end)) {
//            log.error("Время начала и окончания периода не может быть равно");
//            throw new StatisticWrongTimeException("Время начала и окончания периода не может быть равно");
//        }
//    }
}
