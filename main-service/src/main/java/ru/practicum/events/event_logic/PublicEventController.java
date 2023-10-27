package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.event_models.EventFullDto;
import ru.practicum.events.event_models.EventPublicParams;
import ru.practicum.events.event_models.EventShortDto;
import ru.practicum.util.Sorts;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: PublicEventController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:21 PM (UTC+3)
 * Description:
 */

@Transactional
@Slf4j
@RestController
@RequestMapping(path = "/events")
public class PublicEventController {

    private final EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getPublicEventsByParams(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) Sorts sort,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    LocalDateTime rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size,
            HttpServletRequest request) {
        final EventPublicParams params = new EventPublicParams(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        log.info("GET запрос getPublicEventsByParams - params: \n{}, from: \n{}, size: \n{}", params, from, size);
        final List<EventShortDto> response = eventService.getPublicEventsByParams(params, from, size, request);
        log.info("GET ответ getPublicEventsByParams - response: \n{}", response);
        return response;
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable(value = "id") @Min(1) Long id, HttpServletRequest request) {
        log.info("GET запрос getPublicEventById - id: \n{}", id);
        final EventFullDto response = eventService.getPublicEventById(id, request);
        log.info("GET ответ getPublicEventById - response: \n{}", response);
        return response;
    }

}
