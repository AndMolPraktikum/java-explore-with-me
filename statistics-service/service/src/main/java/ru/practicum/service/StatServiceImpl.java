package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.RegistryMapper;
import ru.practicum.RegistryRequest;
import ru.practicum.RegistryResponse;
import ru.practicum.model.Registry;
import ru.practicum.repository.StatServiceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final StatServiceRepository statServiceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getStats(String startString, String endString,
                                           String urisString, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startString, formatter);
        LocalDateTime end = LocalDateTime.parse(endString, formatter);

        List<String> uris = new ArrayList<>();
        if (!urisString.isBlank()) {
            uris = Arrays.asList(urisString.split(","));
        }

        List<RegistryResponse> registryResponseList = new ArrayList<>();
        if (urisString.isBlank() && unique.equals(false)) {
            registryResponseList = statServiceRepository.findRecordsByTimeRange(start, end);
        }
        if (urisString.isBlank() && unique.equals(true)) {
            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUniqueIp(start, end);
        }
        if (!urisString.isBlank() && unique.equals(false)) {
            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUris(start, end, uris);
        }
        if (!urisString.isBlank() && unique.equals(true)) {
            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUrisAndUniqueIp(start, end, uris);
        }

        return registryResponseList;
    }

    @Override
    @Transactional
    public void createRegistry(RegistryRequest registryRequest) {
        Registry registry = RegistryMapper.toRegistryEntity(registryRequest);
        statServiceRepository.save(registry);
    }
}
