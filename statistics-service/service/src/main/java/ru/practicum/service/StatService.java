package ru.practicum.service;

import ru.practicum.RegistryRequest;
import ru.practicum.RegistryResponse;

import java.util.List;

public interface StatService {


    List<RegistryResponse> getStats(String start, String end, String uris, Boolean unique);

    void createRegistry(RegistryRequest registryRequest);
}
