package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.RegistryResponse;
import ru.practicum.model.Registry;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatServiceRepository extends JpaRepository<Registry, Long> {

    @Query("SELECT new ru.practicum.RegistryResponse(r.app, r.uri, COUNT(r.ip)) " +
            "FROM Registry r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY r.app, r.uri")
    List<RegistryResponse> findRecordsByTimeRange(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.RegistryResponse(r.app, r.uri, COUNT(DISTINCT r.ip)) " +
            "FROM Registry r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY r.app, r.uri")
    List<RegistryResponse> findRecordsByTimeRangeAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.RegistryResponse(r.app, r.uri, COUNT(r.ip) AS hits) " +
            "FROM Registry r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2 " +
            "AND r.uri IN ?3 " +
            "GROUP BY r.app, r.uri " +
            "ORDER BY COUNT(r.ip) DESC")
    List<RegistryResponse> findRecordsByTimeRangeAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.RegistryResponse(r.app, r.uri, COUNT(DISTINCT r.ip) AS hits) " +
            "FROM Registry r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2 " +
            "AND r.uri IN ?3 " +
            "GROUP BY r.app, r.uri " +
            "ORDER BY COUNT(r.ip) DESC")
    List<RegistryResponse> findRecordsByTimeRangeAndUrisAndUniqueIp(LocalDateTime start,
                                                                    LocalDateTime end, List<String> uris);

}
