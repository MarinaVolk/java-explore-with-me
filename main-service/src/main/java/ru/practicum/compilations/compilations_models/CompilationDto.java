package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.events.event_models.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private final Long id;

    private final List<EventShortDto> events;

    private final Boolean pinned;

    private final String title;
}
