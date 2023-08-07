package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Request r " +
            "WHERE r.requester.id = ?1 AND r.event.id = ?2 AND r.status = 'CONFIRMED'")
    boolean isUserParticipant(Long userId, Long eventId);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.commentStatus = 'PENDING'")
    List<Comment> findAllByCommentStatusPending(Pageable page);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.author.id = ?1 " +
            "AND c.commentStatus = 'NEED_CORRECTION' ")
    List<Comment> findAllByAuthorIdAndStatus(Long authorId, Pageable page);

    List<Comment> findAllByAuthorId(Long authorId, Pageable page);

    @Query("SELECT c FROM Comment c " +
            "WHERE (:categories IS NULL OR c.event.category.id IN :categories) " +
            "AND (CAST(:rangeStart AS timestamp) IS NULL OR c.publishedOn > :rangeStart) " +
            "AND (CAST(:rangeEnd AS timestamp) IS NULL OR c.publishedOn < :rangeEnd) " +
            "AND c.commentStatus = 'PUBLISHED' ")
    List<Comment> findAllByPublicParam(@Param("categories") List<Long> categories,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable page);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.event.id = :eventId " +
            "AND (CAST(:rangeStart as timestamp) IS NULL OR c.publishedOn > :rangeStart) " +
            "AND (CAST(:rangeEnd as timestamp) IS NULL OR c.publishedOn < :rangeEnd) " +
            "AND c.commentStatus = 'PUBLISHED' ")
    List<Comment> findAllByEventIdAndPublicParam(@Param("eventId") Long eventId,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                 Pageable page);


}
