package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.event_models.EventFullDto;
import ru.practicum.events.event_models.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.util.List;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getEventsByParams(

            @RequestParam(value = "users", required = false) Long[] users,
            @RequestParam(value = "states", required = false) String[] states,
            @RequestParam(value = "categories", required = false) Long[] categories,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<EventFullDto> result = adminEventService.getByParams(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
        return result;
    }

    @PatchMapping("/{id}")
    public EventFullDto updateEventByAdmin(@PathVariable(value = "id") Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest updateRequest) {

        return adminEventService.updateEventByAdmin(eventId, updateRequest);
    }
}
