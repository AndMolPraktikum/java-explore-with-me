package ru.practicum.comments.mapper;

import ru.practicum.comments.dto.CommentFullDto;
import ru.practicum.comments.dto.CommentRequest;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.enums.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static Comment toCommentEntity(CommentRequest commentRequest, User user, Event event) {
        return Comment.builder()
                .header(commentRequest.getHeader())
                .description(commentRequest.getDescription())
                .commentStatus(CommentStatus.PENDING)
                .author(user)
                .event(event)
                .build();
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return new CommentShortDto(
                comment.getId(),
                comment.getHeader(),
                comment.getDescription(),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getEvent().getId()
        );
    }

    public static List<CommentShortDto> toCommentShortDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getHeader(),
                comment.getDescription(),
                comment.getCreatedOn(),
                comment.getPublishedOn(),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getEvent().getId(),
                comment.getCommentStatus(),
                comment.getAdminComment()
        );
    }

    public static List<CommentFullDto> toCommentFullDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }
}
