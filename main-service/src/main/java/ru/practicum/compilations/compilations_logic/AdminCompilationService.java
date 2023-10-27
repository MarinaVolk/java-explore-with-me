package ru.practicum.compilations.compilations_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.compilations_models.*;
import ru.practicum.events.event_models.EventMapper;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.events.event_models.EventShortDto;
import ru.practicum.exceptions.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * File Name: AdminCompilationService.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:37 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompEventRepository compEventRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();

        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }

        compilation.setTitle(newCompilationDto.getTitle());
        compilation = compilationRepository.save(compilation);

        Long compId = compilation.getId();

        Long[] eventIds = new Long[]{};
        if (newCompilationDto.getEvents() != null) {
            eventIds = newCompilationDto.getEvents();
        }

        List<CompEvent> compEventList = new ArrayList<>();

        for (Long eventId : eventIds) {

            CompEvent compEvent = new CompEvent();
            compEvent.setCompId(compId);
            compEvent.setEventId(eventId);
            compEventList.add(compEvent);
        }

        compEventRepository.saveAll(compEventList);

        List<EventShortDto> eventShortDtoList = EventMapper.eventToShortDto(eventRepository.findAllByIdIn(eventIds));

        CompilationDto result = CompMapper.compilationToDto(compilation, eventShortDtoList);

        return result;
    }

    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequests updateRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("- Подборка событий с id=" + compId + " не найдена"));

        if (updateRequest.getPinned() != null) {
            compilation.setPinned(updateRequest.getPinned());
        }

        if (updateRequest.getTitle() != null) {
            compilation.setTitle(updateRequest.getTitle());
        }

        Long[] eventIds = compEventRepository.findAllByCompId(compId).stream()
                .map(CompEvent::getEventId)
                .toArray(Long[]::new);

        if (updateRequest.getEvents() != null) {

            eventIds = updateRequest.getEvents();

            List<CompEvent> compEventList = new ArrayList<>();

            compEventRepository.deleteByCompId(compId);

            for (Long eventId : eventIds) {
                CompEvent compEvent = new CompEvent();
                compEvent.setCompId(compId);
                compEvent.setEventId(eventId);
                compEventList.add(compEvent);
            }

            compEventRepository.saveAll(compEventList);
        }

        List<EventShortDto> eventShortDtos = EventMapper.eventToShortDto(eventRepository.findAllByIdIn(eventIds));
        CompilationDto result = CompMapper.compilationToDto(compilation, eventShortDtos);
        return result;
    }

    @Transactional
    public void delete(Long compId) {

        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("- Подборка событий с id=" + compId + " не найдена");
        }
        compilationRepository.deleteById(compId);
    }
}
