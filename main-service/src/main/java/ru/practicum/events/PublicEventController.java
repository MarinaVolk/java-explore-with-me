package ru.practicum.events;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

/**
 * File Name: PublicEventController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:21 PM (UTC+3)
 * Description:
 */

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {

    private final PublicEventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getByParameters(

            HttpServletRequest request,

            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) Long[] categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size

    ) {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        return eventService.getByParameters(uri, ip,

                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable(value = "id") Long eventId, HttpServletRequest request) {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        return eventService.getById(eventId, uri, ip);
    }
}
