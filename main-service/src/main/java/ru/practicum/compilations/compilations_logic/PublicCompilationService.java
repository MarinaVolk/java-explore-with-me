package ru.practicum.compilations.compilations_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.compilations_models.CompEvent;
import ru.practicum.compilations.compilations_models.CompMapper;
import ru.practicum.compilations.compilations_models.Compilation;
import ru.practicum.compilations.compilations_models.CompilationDto;
import ru.practicum.events.event_models.EventMapper;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.events.event_models.EventShortDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompEventRepository compEventRepository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getByParameters(Boolean pinned, int from, int size) {

        PageRequest pageRequest;

        if (size > 0 && from >= 0) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        } else {
            throw new ValidationException("Размер страницы должен быть больше 0, поле 'from' должно быть больше или равно 0.");
        }

        Iterable<Compilation> compilationList;
        if (pinned != null) {
            compilationList = compilationRepository.findByPinned(pinned, pageRequest);
        } else {
            compilationList = compilationRepository.findAll(pageRequest);
        }

        List<CompilationDto> resultList = getCompilationDtos(compilationList);

        if (resultList.size() > 10) {
            resultList = resultList.subList(0, 10);
        }
        return resultList;
    }

    public CompilationDto getById(Long compId) {
        CompilationDto compilationDto = getCompilationDto(compId);
        return compilationDto;
    }

    private CompilationDto getCompilationDto(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id=" + compId + " не найдена"));

        Long[] eventIds = compEventRepository.findAllByCompId(compId).stream()
                .map(CompEvent::getEventId)
                .toArray(Long[]::new);

        List<EventShortDto> eventShortDtos = EventMapper.eventToShortDto(eventRepository.findAllByIdIn(eventIds));
        return CompMapper.compilationToDto(compilation, eventShortDtos);
    }

    private List<CompilationDto> getCompilationDtos(Iterable<Compilation> compilationList) {

        List<CompilationDto> compilationDtoList = new ArrayList<>();
        List<Long> compIds = new ArrayList<>();
        for (Compilation c : compilationList) {
            compIds.add(c.getId());
        }
        List<CompEvent> compEvents = compEventRepository.findAllByCompIdIn(compIds);

        for (Compilation comp : compilationList) {
            List<EventShortDto> eventsForCompDto = new ArrayList<>();

            for (CompEvent cEvent : compEvents) {
                List<Long> eventIds = new ArrayList<>();
                if (cEvent.getCompId() == comp.getId()) {
                    eventIds.add(cEvent.getEventId());
                }
                Long[] array = new Long[eventIds.size()];
                array = eventIds.toArray(array);

                List<EventShortDto> eventShortDtos = EventMapper.eventToShortDto(eventRepository.findAllByIdIn(array));
                for (Compilation comp1 : compilationList) {
                    compilationDtoList.add(CompMapper.compilationToDto(comp1, eventShortDtos));
                }
            }
        }
        return compilationDtoList;
    }
}
