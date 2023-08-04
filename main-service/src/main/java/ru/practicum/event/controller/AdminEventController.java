package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventAdminRequestParam;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAdminEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<State> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("Входящий запрос GET /admin/events: {}", request.getRequestURI());
        EventAdminRequestParam params = new EventAdminRequestParam(users, states, categories, rangeStart, rangeEnd,
                from, size);
        final List<EventFullDto> eventFullDtoList = eventService.getAdminEvents(params);
        log.info("Исходящий ответ: {}", eventFullDtoList);
        return eventFullDtoList;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateAdminEventById(@PathVariable Long eventId,
                                             @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Входящий запрос PATCH /admin/events/{}: {}", eventId, updateEventAdminRequest);
        final EventFullDto eventFullDto = eventService.updateAdminEventById(eventId, updateEventAdminRequest);
        log.info("Исходящий ответ: {}", eventFullDto);
        return eventFullDto;
    }
}
