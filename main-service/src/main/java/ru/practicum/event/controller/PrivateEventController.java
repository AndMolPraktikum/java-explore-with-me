package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<EventShortDto> getAllUserEvents(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /users/{}/events?from={}&size={}", userId, from, size);
        List<EventShortDto> eventShortDtoList = eventService.getPrivateUserEvents(userId, from, size);
        log.info("Исходящий ответ: {}", eventShortDtoList);
        return eventShortDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Входящий запрос POST /users/{}/events: {}", userId, newEventDto);
        EventFullDto createdEventFullDto = eventService.createEvent(userId, newEventDto);
        log.info("Исходящий ответ: {}", createdEventFullDto);
        return createdEventFullDto;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    private EventFullDto getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Входящий запрос GET /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto = eventService.getPrivateEventById(userId, eventId);
        log.info("Исходящий ответ: {}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    private EventFullDto updateUserEventById(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Входящий запрос PATCH /users/{}/events/{} : {}", userId, eventId, updateEventUserRequest);
        EventFullDto updatedEventFullDto = eventService.updatePrivateUserEventById(userId, eventId,
                updateEventUserRequest);
        log.info("Исходящий ответ: {}", updatedEventFullDto);
        return updatedEventFullDto;
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    private List<ParticipationRequestDto> getUserEventRequest(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Входящий запрос GET /users/{}/events/{}/requests", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtoList = eventService.getUserParticipation(userId, eventId);
        log.info("Исходящий ответ: {}", participationRequestDtoList);
        return participationRequestDtoList;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    private EventRequestStatusUpdateResult getUserEventRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        log.info("Входящий запрос PATCH /users/{}/events/{}/requests : {}", userId, eventId, request);
        EventRequestStatusUpdateResult result = eventService.updateEventStatusFromOwner(userId, eventId, request);
        log.info("Исходящий ответ: {}", result);
        return result;
    }
}
