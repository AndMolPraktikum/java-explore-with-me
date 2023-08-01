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

    //ToDo Поиск событий
    //Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAdminEvents(
             @RequestParam(name = "users", required = false) List<Long> users,  //список id пользователей, чьи события нужно найти
             @RequestParam(name = "states", required = false) List<State> states, //список состояний в которых находятся искомые события
             @RequestParam(name = "categories", required = false) List<Long> categories, //список id категорий в которых будет вестись поиск
             @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart, //дата и время не раньше которых должно произойти событие
             @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd, //дата и время не позже которых должно произойти событие
             @RequestParam(name = "from", defaultValue = "0") int from,
             @RequestParam(name = "size", defaultValue = "10") int size,
             HttpServletRequest request) {
        log.info("Входящий запрос GET /admin/events: {}", request.getRequestURI());
        EventAdminRequestParam params = new EventAdminRequestParam(users, states, categories, rangeStart, rangeEnd, from, size);
        final List<EventFullDto> eventFullDtoList = eventService.getAdminEvents(params);
        log.info("Исходящий ответ: {}", eventFullDtoList);
        return eventFullDtoList;
    }

    //ToDo Редактирование данных события и его статуса (отклонение/публикация)
    //Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:
    //дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
    //событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
    //событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
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
