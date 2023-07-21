package ru.practicum;

import ru.practicum.model.Registry;

public class RegistryMapper {

    public static Registry toRegistryEntity(RegistryRequest registryRequest) {
        return new Registry(
                registryRequest.getApp(),
                registryRequest.getUri(),
                registryRequest.getIp(),
                registryRequest.getTimestamp()
        );
    }
}
