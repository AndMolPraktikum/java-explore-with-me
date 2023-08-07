package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentRequest;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    @Autowired
    private final CommentService commentService;

    //ToDo Получение комментариев добавленных этим пользователем
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getAllUserComment(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean onlyReturned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /users/{}/comments?onlyReturned={}&from={}&size={}",
                onlyReturned, userId, from, size);
        List<CommentFullDto> commentShortDtoList = commentService.getPrivateUserComments(userId, onlyReturned, from, size);
        log.info("Исходящий ответ: {}", commentShortDtoList);
        return commentShortDtoList;
    }

    //ToDo Получение полной информации о комментарии добавленном текущим пользователем
    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto getUserCommentById(@PathVariable Long userId,
                                             @PathVariable Long comId) {
        log.info("Входящий запрос GET /users/{}/comments/{}", userId, comId);
        CommentFullDto commentById = commentService.getUserCommentById(userId, comId);
        log.info("Исходящий ответ: {}", commentById);
        return commentById;
    }

    //ToDo Добавление нового комментария
    //Добавление нового комментария не может быть ранее начала события
    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestBody @Valid CommentRequest commentRequest) {
        log.info("Входящий запрос POST /users/{}/comments/{} : {}", userId, eventId, commentRequest);
        CommentFullDto createdComment = commentService.createComment(userId, eventId, commentRequest);
        log.info("Исходящий ответ: {}", createdComment);
        return createdComment;
    }

    //ToDo Исправление своего комментария
    //Исправить комментарий можно только в случае, если админ вернул его на корректировку
    //Исправить отклоненный комментарий нельзя.
    //После исправления комментарий снова идет на модерацию к админу
    @PatchMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateComment(@PathVariable Long userId,
                                        @PathVariable Long comId,
                                        @RequestBody CommentRequest commentRequest) {
        log.info("Входящий запрос PATCH /users/{}/comments/{} : {}", userId, comId, commentRequest);
        CommentFullDto updatedComment = commentService.updateUserComment(userId, comId, commentRequest);
        log.info("Исходящий ответ: {}", updatedComment);
        return updatedComment;
    }


    //ToDo Удаление своего комментария
    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long comId) {
        log.info("Входящий запрос DELETE /users/{}/comments/{}", userId, comId);
        commentService.deleteUserComment(userId, comId);
    }
}
