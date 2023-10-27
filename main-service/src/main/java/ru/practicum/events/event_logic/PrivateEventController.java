package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.event_models.EventFullDto;
import ru.practicum.events.event_models.EventShortDto;
import ru.practicum.events.event_models.NewEventDto;
import ru.practicum.events.event_models.UpdateEventUserRequest;
import ru.practicum.requests.request_models.EventRequestStateUpdateRequest;
import ru.practicum.requests.request_models.EventRequestStateUpdateResult;
import ru.practicum.requests.request_models.RequestDto;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

/**
 * File Name: PrivateEventController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:20 PM (UTC+3)
 * Description:
 */

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService eventService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable(value = "userId") Long initiatorId,
                            @RequestBody @Valid NewEventDto newEventDto) {

        return eventService.add(initiatorId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getByUserId(@PathVariable(value = "userId") Long initiatorId,
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {

        return eventService.getByInitiatorId(initiatorId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable(value = "userId") Long initiatorId,
                                @PathVariable(value = "eventId") Long eventId) {

        return eventService.getById(initiatorId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByInitiator(@PathVariable(value = "userId") Long initiatorId,
                                               @PathVariable(value = "eventId") Long eventId,
                                               @RequestBody @Valid UpdateEventUserRequest updateRequest) {

        return eventService.updateEventByInitiator(initiatorId, eventId, updateRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable(value = "userId") Long initiatorId,
                                        @PathVariable(value = "eventId") Long eventId) {

        return eventService.getRequests(initiatorId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStateUpdateResult updateRequestStatusFromInitiator(

            @PathVariable(value = "userId") Long initiatorId,
            @PathVariable(value = "eventId") Long eventId,
            @RequestBody EventRequestStateUpdateRequest statusUpdateRequest) {

        return eventService.updateRequestStatusFromInitiator(initiatorId, eventId, statusUpdateRequest);
    }
}
