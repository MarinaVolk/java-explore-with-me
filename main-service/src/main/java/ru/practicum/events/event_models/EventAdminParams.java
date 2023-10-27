package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.event_models.EventState;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: EventAdminParams.java
 * Author: Marina Volkova
 * Date: 2023-10-27,   8:53 PM (UTC+3)
 * Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminParams {
    private List<Long> users;

    private List<EventState> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;
}
