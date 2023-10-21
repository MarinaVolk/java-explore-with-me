package ru.practicum.compilations;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.events.EventShortDto;

import java.util.List;

/**
 * File Name: CompilationDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:29 PM (UTC+3)
 * Description:
 */

@Data
public class CompilationDto {
    private final Long id;

    private final List<EventShortDto> events;

    private final Boolean pinned;

    private final String title;
}
