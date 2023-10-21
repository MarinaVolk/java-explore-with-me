package ru.practicum.statslogic;

import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * File Name: ru.practicum.StatsLogic.StatsService.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:40 PM (UTC+3)
 * Description:
 */

public interface StatsService {

    void saveStats(HitDto statsHitDto);

    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
