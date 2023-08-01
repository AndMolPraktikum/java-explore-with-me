package ru.practicum.service;

import ru.practicum.RegistryRequest;
import ru.practicum.RegistryResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {


    List<RegistryResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void createRegistry(RegistryRequest registryRequest);
}
