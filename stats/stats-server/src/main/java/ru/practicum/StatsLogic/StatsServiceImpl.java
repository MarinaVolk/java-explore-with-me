package ru.practicum.StatsLogic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Exceptions.ValidationException;
import ru.practicum.HitDto;
import ru.practicum.Models.Hit;
import ru.practicum.Models.HitMapper;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: ru.practicum.StatsLogic.StatsServiceImpl.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:41 PM (UTC+3)
 * Description:
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveStats(HitDto dto) {
        Hit statHit = repository.save(HitMapper.statsHitDtoToStatHit(dto));
        log.info("Получена статистика {}", statHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            log.info("Время окончания не может быть раньше времени начала");
            throw new ValidationException("Время окончания не может быть раньше времени начала");
        }

        if (uris.isEmpty()) {
            if (unique) {
                log.info("Получить всю статистику где isUnique {} ", unique);
                return repository.getStatsByUniqueIp(start, end);
            } else {
                log.info("Получить всю статистику где isUnique {} ", unique);
                return repository.getAllStats(start, end);
            }
        } else {
            if (unique) {
                log.info("Получить всю статистику где isUnique {} и uris {} ", unique, uris);
                return repository.getStatsByUrisByUniqueIp(start, end, uris);
            } else {
                log.info("Получить всю статистику где isUnique {} и uris {} ", unique, uris);
                return repository.getAllStatsByUris(start, end, uris);
            }
        }
    }
}
