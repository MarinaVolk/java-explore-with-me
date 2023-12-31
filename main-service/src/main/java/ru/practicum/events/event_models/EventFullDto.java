package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.category.category_models.CategoryDto;
import ru.practicum.users.user_models.UserShortDto;

import java.time.LocalDateTime;

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
