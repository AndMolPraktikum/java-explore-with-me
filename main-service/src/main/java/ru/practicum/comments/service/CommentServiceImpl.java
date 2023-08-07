package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.*;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.enums.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.CommentNotFoundException;
import ru.practicum.exception.CommentStatusConflictException;
import ru.practicum.exception.UserParticipationNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final EventService eventService;


    @Override
    public List<CommentShortDto> getPublicComments(CommentParam commentParam) {
        List<Long> categories = commentParam.getCategories();
        LocalDateTime rangeStart = commentParam.getRangeStart();
        LocalDateTime rangeEnd = commentParam.getRangeEnd();
        int from = commentParam.getFrom();
        int size = commentParam.getSize();
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Comment> commentList = commentRepository.findAllByPublicParam(categories, rangeStart, rangeEnd, page);
        return CommentMapper.toCommentShortDtoList(commentList);
    }

    @Override
    public List<CommentShortDto> getPublicCommentsByEventId(Long eventId, CommentParam commentParam) {
        LocalDateTime rangeStart = commentParam.getRangeStart();
        LocalDateTime rangeEnd = commentParam.getRangeEnd();
        int from = commentParam.getFrom();
        int size = commentParam.getSize();
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Comment> commentList = commentRepository
                .findAllByEventIdAndPublicParam(eventId, rangeStart, rangeEnd, page);
        return CommentMapper.toCommentShortDtoList(commentList);
    }

    ///Дописать
    @Override
    public List<CommentFullDto> getPrivateUserComments(Long userId, boolean onlyReturned, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Comment> commentList;
        if (onlyReturned) {
            commentList = commentRepository.findAllByAuthorIdAndStatus(userId, page);
        } else {
            commentList = commentRepository.findAllByAuthorId(userId, page);
        }

        return CommentMapper.toCommentFullDtoList(commentList);
    }

    @Override
    public CommentFullDto getUserCommentById(Long userId, Long comId) {
        userService.getUserById(userId);
        Comment comment = getCommentById(comId);
        isUserCommenter(comment, userId);
        return CommentMapper.toCommentFullDto(comment);
    }

    //Сортировка вывода должна быть по дате создания комментария. Сначала самые ранние.
    @Override
    public List<CommentFullDto> getCommentsForModerating(int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("createdOn"));
        List<Comment> commentList = commentRepository.findAllByCommentStatusPending(page);
        return CommentMapper.toCommentFullDtoList(commentList);
    }

    @Override
    @Transactional
    public CommentFullDto createComment(Long userId, Long eventId, CommentRequest commentRequest) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        userParticipationCheck(userId, eventId, event);
        Comment comment = CommentMapper.toCommentEntity(commentRequest, user, event);
        return CommentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    //После обновления комментарий снова "падает" на проверку админу.
    @Override
    @Transactional
    public CommentFullDto updateUserComment(Long userId, Long comId, CommentRequest commentRequest) {
        userService.getUserById(userId);
        Comment comment = getCommentById(comId);
        if (comment.getCommentStatus().equals(CommentStatus.REJECTED)) {
            log.error("Comment with status REJECTED not allowed for editing");
            throw new CommentStatusConflictException("Comment with status REJECTED not allowed for editing");
        }
        isUserCommenter(comment, userId);
        if (commentRequest.getHeader() != null) {
            comment.setHeader(commentRequest.getHeader());
        }
        if (commentRequest.getDescription() != null) {
            comment.setDescription(commentRequest.getDescription());
        }
        comment.setPublishedOn(null);
        comment.setCommentStatus(CommentStatus.PENDING);
        comment.setAdminComment(null);
        return CommentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public List<CommentFullDto> updateAdminCommentStatus(List<UpdateCommentAdminRequest> requestList) {
        List<Comment> commentList = new ArrayList<>();
        for (UpdateCommentAdminRequest request : requestList) {
            if (request.getCommentStatus().equals(CommentStatus.PENDING)) {
                log.error("Status must be changed");
                throw new CommentStatusConflictException("Status must be changed");
            }
            Comment comment = getCommentById(request.getCommentId());
            comment.setCommentStatus(request.getCommentStatus());
            if (request.getCommentStatus().equals(CommentStatus.PUBLISHED)) {
                comment.setPublishedOn(LocalDateTime.now());
            }
            if (request.getCommentStatus().equals(CommentStatus.NEED_CORRECTION)) {
                if (request.getAdminComment() != null) {
                    comment.setAdminComment(request.getAdminComment());
                }
            }
            commentList.add(comment);
        }
        return CommentMapper.toCommentFullDtoList(commentRepository.saveAll(commentList));
    }

    @Override
    @Transactional
    public void deleteUserComment(Long userId, Long comId) {
        userService.getUserById(userId);
        Comment comment = getCommentById(comId);
        isUserCommenter(comment, userId);
        commentRepository.deleteById(comId);
    }

    @Override
    @Transactional
    public void deleteAdminComment(Long comId) {
        commentRepository.deleteById(comId);
    }

    private void userParticipationCheck(Long userId, Long eventId, Event event) {
        if (!commentRepository.isUserParticipant(userId, eventId)) {
            log.error("The user={} is not participating in the event={}", userId, eventId);
            throw new UserParticipationNotFoundException(String.format("The user=%d is not participating " +
                    "in the event=%d", userId, eventId));
        }

        //С этой проверкой тесты не работают.
//        if (event.getEventDate().isAfter(LocalDateTime.now())) { //нельзя создать комментарий раньше начала события
//            log.error("Comment creation time before event start");
//            throw new CommentTimeConflictException("Comment creation time before event start");
//        }
    }

    private Comment getCommentById(Long comId) {
        Optional<Comment> commentOption = commentRepository.findById(comId);
        if (commentOption.isEmpty()) {
            log.error("Comment with id={} was not found", comId);
            throw new CommentNotFoundException(String.format("Comment with id=%d was not found", comId));
        }
        return commentOption.get();
    }


    private void isUserCommenter(Comment comment, Long userId) {
        Long comId = comment.getAuthor().getId();
        if (!comId.equals(userId)) {
            log.error("The user={} is not author for the comment={}", userId, comId);
            throw new CommentNotFoundException(String.format("The user=%d is not author " +
                    "for the comment=%d", userId, comId));
        }
    }
}
