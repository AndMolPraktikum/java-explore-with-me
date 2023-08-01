package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.RegistryMapper;
import ru.practicum.RegistryRequest;
import ru.practicum.RegistryResponse;
import ru.practicum.exception.StatisticWrongTimeException;
import ru.practicum.model.Registry;
import ru.practicum.repository.StatServiceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    @Autowired
    private final StatServiceRepository statServiceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RegistryResponse> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, Boolean unique) {
        checkTime(start, end);

        List<RegistryResponse> registryResponseList = new ArrayList<>();


//        if (uris.isEmpty() && unique.equals(false)) {
//            registryResponseList = statServiceRepository.findRecordsByTimeRange(start, end);
//        }
//        if (uris.isEmpty() && unique.equals(true)) {
//            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUniqueIp(start, end);
//        }
//        if (!uris.isEmpty() && unique.equals(false)) {
//            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUris(start, end, uris);
//        }
//        if (!uris.isEmpty() && unique.equals(true)) {
//            registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUrisAndUniqueIp(start, end, uris);
//        }

        if (unique) {
            if (uris.isEmpty()) {
                registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUniqueIp(start, end);
            } else {
                registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUrisAndUniqueIp(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                registryResponseList = statServiceRepository.findRecordsByTimeRange(start, end);
            } else {
                registryResponseList = statServiceRepository.findRecordsByTimeRangeAndUris(start, end, uris);
            }
        }

        return registryResponseList;
    }

    @Override
    @Transactional
    public void createRegistry(RegistryRequest registryRequest) {
        Registry registry = RegistryMapper.toRegistryEntity(registryRequest);
        registry = statServiceRepository.save(registry);
        //ToDo убрать лог и возврат
        log.info("StatServiceImpl. запрос сохранен в базу: {}", registry);
    }

    private void checkTime(LocalDateTime start, LocalDateTime end) {
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
