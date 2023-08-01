package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.dto.RegistryRequest;
import ru.practicum.client.dto.RegistryResponse;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainClient {

    private static final String APP = "ewm-main-service";

    private static final LocalDateTime START_TIME = LocalDateTime.now().minusDays(30);

    private final StatisticWebClient statisticWebClient;

    public void saveRegistry(String uri, String ip) {
        RegistryRequest request = new RegistryRequest(APP, uri, ip, LocalDateTime.now());
        statisticWebClient.createRegistry(request);
    }

    public long getStatisticById(Long eventId) {
        List<RegistryResponse> responseList = statisticWebClient.getStats(START_TIME, LocalDateTime.now(),
                List.of("/events/" + eventId), true);
        if (responseList.isEmpty()) {
            //ToDo убрать лог
            log.info("responseList пустой: {}", responseList);
            return 0L;
        }
        //ToDo убрать лог
        log.info("responseList: {}", responseList);
        return responseList.get(0).getHits();
    }

    public List<EventShortDto> getStatisticShort(List<EventShortDto> eventShortDtoList) {
        //List<Long> urisIds =  eventShortDtoList.stream().map(EventShortDto::getId).collect(Collectors.toList());
        List<String> uris = eventShortDtoList.stream()
                .map(EventShortDto::getId)
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());
        List<RegistryResponse> responseList = statisticWebClient.getStats(START_TIME, LocalDateTime.now(),
                uris, true);
        Map<Long, Long> idViewsMap = responseList.stream()
                .collect(Collectors.toMap(
                        response -> Long.parseLong(response.getUri().substring("/events/".length())),
                        RegistryResponse::getHits
                ));
        eventShortDtoList.forEach(event -> {
            Long views = idViewsMap.getOrDefault(event.getId(), 0L);
            event.setViews(views);
        });
        return eventShortDtoList;
    }

    public List<EventFullDto> getStatisticFull(List<EventFullDto> eventFullDtoList) {
        //List<Long> urisIds =  eventShortDtoList.stream().map(EventShortDto::getId).collect(Collectors.toList());
        List<String> uris = eventFullDtoList.stream()
                .map(EventFullDto::getId)
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());
        List<RegistryResponse> responseList = statisticWebClient.getStats(START_TIME, LocalDateTime.now(),
                uris, true);
        Map<Long, Long> idViewsMap = responseList.stream()
                .collect(Collectors.toMap(
                        response -> Long.parseLong(response.getUri().substring("/events/".length())),
                        RegistryResponse::getHits
                ));
        eventFullDtoList.forEach(event -> {
            Long views = idViewsMap.getOrDefault(event.getId(), 0L);
            event.setViews(views);
        });
        return eventFullDtoList;
    }
}
