package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.category.category_models.CategoryDto;
import ru.practicum.users.user_models.UserShortDto;

/**
 * File Name: EventShortDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:13 PM (UTC+3)
 * Description:
 */

@Data
public class EventShortDto {
    private final Long id;
    private final String annotation;
    private final CategoryDto category;
    private final Integer confirmedRequests;
    private final String eventDate;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private final Integer views;
}
