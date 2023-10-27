package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.Sorts;

import java.time.LocalDateTime;
import java.util.List;

/**
 * File Name: EventPublicParams.java
 * Author: Marina Volkova
 * Date: 2023-10-27,   8:44 PM (UTC+3)
 * Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicParams {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Sorts sort;
}
