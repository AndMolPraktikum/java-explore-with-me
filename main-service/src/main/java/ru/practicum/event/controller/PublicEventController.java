package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.SortVariant;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventPublicRequestParam;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getPublicEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortVariant sortVariant,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            HttpServletRequest request) {
        log.info("Входящий запрос GET /events: {}", request.getRequestURI());
        EventPublicRequestParam params = new EventPublicRequestParam(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sortVariant, from, size);
        List<EventShortDto> eventShortDtoList = eventService.getPublicEvents(params, request.getRemoteAddr());
        log.info("Исходящий ответ: {}", eventShortDtoList);
        return eventShortDtoList;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublicEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Входящий запрос GET /events/{}", id);
        EventFullDto eventFullDto = eventService.getPublicEventById(id, request.getRemoteAddr());
        log.info("Исходящий ответ: {}", eventFullDto);
        return eventFullDto;
    }
}
