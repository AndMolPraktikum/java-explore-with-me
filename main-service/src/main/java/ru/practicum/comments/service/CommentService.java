package ru.practicum.comments.service;

import ru.practicum.comments.dto.*;

import java.util.List;

public interface CommentService {


    List<CommentShortDto> getPublicComments(CommentParam commentParam);

    List<CommentShortDto> getPublicCommentsByEventId(Long eventId, CommentParam commentParam);

    List<CommentFullDto> getPrivateUserComments(Long userId, boolean onlyReturned, int from, int size);

    CommentFullDto getUserCommentById(Long userId, Long comId);

    List<CommentFullDto> getCommentsForModerating(int from, int size);

    CommentFullDto createComment(Long userId, Long eventId, CommentRequest commentRequest);

    CommentFullDto updateUserComment(Long userId, Long comId, CommentRequest commentRequest);

    List<CommentFullDto> updateAdminCommentStatus(List<UpdateCommentAdminRequest> requestList);

    void deleteUserComment(Long userId, Long comId);

    void deleteAdminComment(Long comId);
}
