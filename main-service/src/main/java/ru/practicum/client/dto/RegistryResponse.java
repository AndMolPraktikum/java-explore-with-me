package ru.practicum.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryResponse {

    private String app;

    private String uri;

    private Long hits;
}
