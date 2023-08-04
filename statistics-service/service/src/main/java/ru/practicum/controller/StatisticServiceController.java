package ru.practicum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.RegistryRequest;
import ru.practicum.RegistryResponse;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceController {

    @Autowired
    private final StatService statService;

    @GetMapping("/stats")
    public List<RegistryResponse> get(
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(name = "uris", defaultValue = "") List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Service. Входящий запрос GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        final List<RegistryResponse> responseList = statService.getStats(start, end, uris, unique);
        log.info("Service. Исходящий ответ: {}", responseList);
        return responseList;
    }

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@RequestBody RegistryRequest registryRequest) {
        log.info("Service. Входящий запрос POST /hit. RegistryRequest: {}", registryRequest);
        statService.createRegistry(registryRequest);
    }
}
