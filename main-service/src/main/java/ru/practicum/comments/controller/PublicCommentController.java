package ru.practicum.comments.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentParam;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    @Autowired
    private final CommentService commentService;

    //ToDo это публичный эндпоинт, к выдаче должны быть только опубликованные комментарии
    //текстовый поиск (по заголовку и подробному описанию) должен быть без учета регистра букв
    //если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать комментарии, которые произойдут позже текущей даты и времени
    //комментарии должны быть отсортированы по id события по дате публикации от новых к старым
    //В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<CommentShortDto> getComments(
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            HttpServletRequest request) {
        log.info("Входящий запрос GET /comments: {}", request.getRequestURI());
        CommentParam commentParam = new CommentParam(categories, rangeStart, rangeEnd, from, size);
        List<CommentShortDto> commentShortDtoList = commentService.getPublicComments(commentParam);
        log.info("Исходящий ответ: {}", commentShortDtoList);
        return commentShortDtoList;
    }

    //ToDo комментарии должны быть отсортированы по дате публикации от новых к старым
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    private List<CommentShortDto> getCommentsByEventId(
            @PathVariable Long eventId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            HttpServletRequest request) {
        log.info("Входящий запрос GET /comments/{}: {}", eventId, request.getRequestURI());
        CommentParam commentParam = CommentParam.builder()
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        List<CommentShortDto> commentShortDtoList = commentService.getPublicCommentsByEventId(eventId, commentParam);
        log.info("Исходящий ответ: {}", commentShortDtoList);
        return commentShortDtoList;
    }
}
