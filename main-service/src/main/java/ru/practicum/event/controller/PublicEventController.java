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

    //ToDo Получение событий с возможностью фильтраций
    //Обратите внимание:
    //это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
    //текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
    //если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
    //информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
    //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,  //текст для поиска в содержимом аннотации и подробном описании события
                                               @RequestParam(required = false) List<Long> categories, //список id категорий в которых будет вестись поиск
                                               @RequestParam(required = false) Boolean paid, //поиск только платных/бесплатных событий
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart, //дата и время не раньше которых должно произойти событие
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd, //дата и время не позже которых должно произойти событие
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable, //только события у которых не исчерпан лимит запросов на участие
                                               @RequestParam(required = false) SortVariant sortVariant, //Вариант сортировки: по дате события или по количеству просмотров
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

    //ToDo Получение подробной информации об опубликованном событии по Id
    //Обратите внимание:
    //событие должно быть опубликовано
    //информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
    //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
    //В случае, если события с заданным id не найдено, возвращает статус код 404
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublicEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Входящий запрос GET /events/{}", id);
        EventFullDto eventFullDto = eventService.getPublicEventById(id, request.getRemoteAddr());
        log.info("Исходящий ответ: {}", eventFullDto);
        return eventFullDto;
    }
}
