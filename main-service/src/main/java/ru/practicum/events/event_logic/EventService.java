package ru.practicum.events.event_logic;

import ru.practicum.events.event_models.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size, HttpServletRequest request);

    EventFullDto createEvent(Long userId, NewEventDto eventRequestDto);

    EventFullDto getEventById(Long userId, Long eventId, HttpServletRequest request);

    EventFullDto updateEventByUser(UpdateEventUserRequest event, Long userId, Long eventId);

    List<EventFullDto> getAdminEventsByParams(Long[] users,
                                              String[] states,
                                              Long[] categories,
                                              String rangeStart,
                                              String rangeEnd,
                                              int from,
                                              int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    List<EventShortDto> getPublicEventsByParams(EventPublicParams params, Integer from, Integer size,
                                                HttpServletRequest request);

    EventFullDto getPublicEventById(Long id, HttpServletRequest request);
}
