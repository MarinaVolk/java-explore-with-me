package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import ru.practicum.events.event_models.EventShortDto;

import java.util.List;

public class CompMapper {
    public static CompilationDto compilationToDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), events, compilation.getPinned(), compilation.getTitle());
    }
}
