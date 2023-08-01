package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    @Autowired
    private final EventService eventService;

    //ToDo Получение событий, добавленных этим пользователем
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<EventShortDto> getAllUserEvents(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.info("Входящий запрос GET /users/{}/events?from={}&size={}", userId, from, size);
        List<EventShortDto> eventShortDtoList = eventService.getPrivateUserEvents(userId, from, size);
        log.info("Исходящий ответ: {}", eventShortDtoList);
        return eventShortDtoList;
    }

    //ToDo Добавление нового события
    //Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Входящий запрос POST /users/{}/events: {}", userId, newEventDto);
        EventFullDto createdEventFullDto = eventService.createEvent(userId, newEventDto);
        log.info("Исходящий ответ: {}", createdEventFullDto);
        return createdEventFullDto;
    }

    //ToDo Получение полной информации о событии добавленном текущим пользователем
    //В случае, если события с заданным id не найдено, возвращает статус код 404
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    private EventFullDto getUserEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Входящий запрос GET /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto = eventService.getPrivateEventById(userId, eventId);
        log.info("Исходящий ответ: {}", eventFullDto);
        return eventFullDto;
    }

    //ToDo Изменение события добавленного текущим пользователем
    //изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
    //дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    private EventFullDto updateUserEventById(@PathVariable Long userId,
                                             @PathVariable Long eventId,
                                             @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("Входящий запрос PATCH /users/{}/events/{} : {}", userId, eventId, updateEventUserRequest);
        EventFullDto updatedEventFullDto = eventService.updatePrivateUserEventById(userId, eventId, updateEventUserRequest);
        log.info("Исходящий ответ: {}", updatedEventFullDto);
        return updatedEventFullDto;
    }

    //ToDo Получение информации о запросах на участие в событии текущего пользователя
    //В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    private List<ParticipationRequestDto> getUserEventRequest(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Входящий запрос GET /users/{}/events/{}/requests", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtoList = eventService.getUserParticipation(userId, eventId);
        log.info("Исходящий ответ: {}", participationRequestDtoList);
        return participationRequestDtoList;
    }

    //ToDo Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
    //статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
    //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    private EventRequestStatusUpdateResult getUserEventRequest(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Входящий запрос PATCH /users/{}/events/{}/requests : {}", userId, eventId, eventRequestStatusUpdateRequest);
        EventRequestStatusUpdateResult result = eventService.updateEventStatusFromOwner(userId, eventId,
                eventRequestStatusUpdateRequest);
        log.info("Исходящий ответ: {}", result);
        return result;
    }
}
