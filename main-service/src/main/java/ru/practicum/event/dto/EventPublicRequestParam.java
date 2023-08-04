package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.SortVariant;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicRequestParam {

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;

    private SortVariant sortVariant;

    private int from;

    private int size;
}
