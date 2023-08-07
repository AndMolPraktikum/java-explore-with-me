package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.UpdateCommentAdminRequest;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {

    @Autowired
    private final CommentService commentService;

    //ToDo Поиск всех комментариев требующих модерации
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getCommentsForModerating(@RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /admin/comments?from={}&size={}", from, size);
        List<CommentFullDto> commentShortDtoList = commentService.getCommentsForModerating(from, size);
        log.info("Исходящий ответ: {}", commentShortDtoList);
        return commentShortDtoList;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> updateAdminCommentStatus(@RequestBody List<UpdateCommentAdminRequest> requestList) {
        log.info("Входящий запрос PATCH /admin/comments: {}", requestList);
        List<CommentFullDto> updatedCommentsStatus = commentService.updateAdminCommentStatus(requestList);
        log.info("Исходящий ответ: {}", updatedCommentsStatus);
        return updatedCommentsStatus;
    }

    //ToDo Удаление комментария админом
    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long comId) {
        log.info("Входящий запрос DELETE /admin/comments/{}", comId);
        commentService.deleteAdminComment(comId);
    }
}
