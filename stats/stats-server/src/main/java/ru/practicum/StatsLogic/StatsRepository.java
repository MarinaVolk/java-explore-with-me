package ru.practicum.StatsLogic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.Models.Hit;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: ru.practicum.StatsLogic.StatsRepository.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:38 PM (UTC+3)
 * Description:
 */

public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(distinct s.ip))" +
            "from Hit as s " +
            "where s.timestamp between :start and :end " +
            "group by s.app, s.uri " +
            "order by count(distinct(s.ip)) desc")
    List<ViewStatsDto> getStatsByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(s.ip))" +
            "from Hit as s " +
            "where s.timestamp between :start and :end " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(distinct s.ip))" +
            "from Hit as s " +
            "where s.timestamp between :start and :end " +
            "and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(distinct(s.ip)) desc")
    List<ViewStatsDto> getStatsByUrisByUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ViewStatsDto(s.app, s.uri, count(s.ip))" +
            "from Hit as s " +
            "where s.timestamp between :start and :end " +
            "and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<ViewStatsDto> getAllStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
