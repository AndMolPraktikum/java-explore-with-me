package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.State;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE (:initiators is NULL OR e.initiator.id IN :initiators) " +
            "AND (:states is NULL OR e.state IN :states) " +
            "AND (:categories is NULL OR e.category.id IN :categories) " +
            "AND (CAST(:rangeStart as timestamp) is NULL OR e.eventDate > :rangeStart) " +
            "AND (CAST(:rangeEnd as timestamp) is NULL OR e.eventDate < :rangeEnd)")
    List<Event> findAllByInitiatorsAndStatesAndCategoriesAndTimeRange(@Param("initiators") List<Long> initiators,
                                                                      @Param("states") List<State> states,
                                                                      @Param("categories") List<Long> categories,
                                                                      @Param("rangeStart") LocalDateTime rangeStart,
                                                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                                                      Pageable page);

    @Query("SELECT COUNT(r.id) FROM Request r WHERE r.event.id = ?1 AND r.status like 'CONFIRMED'")
    int getCountByEventIdWhereStatusConfirmed(Long eventId);

    @Query("SELECT e FROM Event e WHERE e.initiator.id = ?1")
    List<Event> findAllByInitiatorId(Long userId, Pageable page);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text is NULL OR UPPER(e.annotation) like UPPER(concat('%', :text, '%')) or UPPER(e.description) like UPPER(concat('%', :text, '%'))) " +
            "AND (:categories is NULL OR e.category.id IN :categories) " +
            "AND (:paid is NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart as timestamp) is NULL OR e.eventDate > :rangeStart) " +
            "AND (CAST(:rangeEnd as timestamp) is NULL OR e.eventDate < :rangeEnd) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (:onlyAvailable is NULL OR e.participantLimit > CAST((SELECT COUNT(r.id) FROM Request r WHERE r.event = e.id AND r.status = 'CONFIRMED') AS int) OR e.participantLimit = 0)")
    List<Event> findAllByCriteria(@Param("text") String text, @Param("categories") List<Long> categories,
                                  @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  @Param("onlyAvailable") Boolean onlyAvailable,
                                  Pageable page);
}
