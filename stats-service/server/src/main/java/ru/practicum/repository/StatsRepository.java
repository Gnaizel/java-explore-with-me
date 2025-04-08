package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.HitBody;
import ru.practicum.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatsRepository extends JpaRepository<HitBody, Long> {


    @Query(value = "SELECT new ru.practicum.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM HitBody AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStatsByDates(Timestamp start, Timestamp end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM HitBody AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStatsByDatesUniqueIp(Timestamp start, Timestamp end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM HitBody AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStatsByDatesAllUris(Timestamp start, Timestamp end);

    @Query("SELECT new ru.practicum.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM HitBody AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findStatsByDatesUniqueIpAllUris(Timestamp start, Timestamp end);

}
