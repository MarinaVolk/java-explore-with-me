package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import ru.practicum.events.event_models.EventShortDto;

import java.util.List;

/**
 * File Name: CompMapper.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   8:03 PM (UTC+3)
 * Description:
 */
public class CompMapper {
    public static CompilationDto compilationToDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), events, compilation.getPinned(), compilation.getTitle());
    }
}
