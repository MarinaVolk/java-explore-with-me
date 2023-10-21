package ru.practicum.events;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.users.UserShortDto;

import java.time.LocalDateTime;

/**
 * File Name: EventFullDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:12 PM (UTC+3)
 * Description:
 */

@Data
public class EventFullDto {
    private final Long id;

    private final String annotation;

    private final CategoryDto category;

    private final Integer confirmedRequests;

    private final String createdOn;

    private final String description;

    private final String eventDate;

    private final UserShortDto initiator;

    private final Location location;

    private final Boolean paid;

    private final Integer participantLimit;

    private final LocalDateTime publishedOn;

    private final Boolean requestModeration;

    private final EventState state;

    private final String title;

    private final Integer views;

    @Override
    public String toString() {
        return "EventFullDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn=" + createdOn +
                ", eventDate=" + eventDate +
                ", initiator=" + initiator +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn=" + publishedOn +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", views=" + views +
                '}';
    }
}
