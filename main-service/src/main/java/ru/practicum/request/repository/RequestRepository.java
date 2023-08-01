package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT COUNT(r.id) FROM Request r WHERE r.event.id = ?1 AND r.status like 'CONFIRMED'")
    int getCountByEventIdWhereStatusConfirmed(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);


//    @Query("SELECT (COUNT(r) > 0) FROM Request r WHERE r.requester.id = ?1 AND r.event.id = ?2;")
//    boolean requestIsAlreadyExist(Long userId, Long eventId);
}
