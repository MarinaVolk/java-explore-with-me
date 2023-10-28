package ru.practicum.statslogic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * File Name: ru.practicum.StatsLogic.StatsController.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:37 PM (UTC+3)
 * Description:
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveStatsHit(@RequestBody @Valid HitDto hitDto) {
        log.info("Received POST to /hit");
        statsService.saveStats(hitDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getViewStats(
            @RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(value = "uris", defaultValue = "") List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique
    ) {
        return statsService.getStats(start, end, uris, unique);
    }
}
