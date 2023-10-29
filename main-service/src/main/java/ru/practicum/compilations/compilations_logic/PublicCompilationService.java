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
import ru.practicum.events.event_models.Event;
import ru.practicum.events.event_models.EventMapper;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.events.event_models.EventShortDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;

import java.util.*;
import java.util.stream.Collectors;


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

        // забираем все события по списку compIds
        List<CompEvent> compEvents = compEventRepository.findAllByCompIdIn(compIds);

        // строим мапу вида {compId: список eventId}
        // также собираем список eventId, которые вообще нужны (для SQL запроса)
        Map<Long, List<Long>> map = new HashMap<>();
        List<Long> globalEventIds = new ArrayList<>();
        for (Compilation comp : compilationList) {
            List<Long> cEventIds = new ArrayList<>();
            for (CompEvent cEvent : compEvents) {
                if (comp.getId().equals(cEvent.getCompId())) {
                    cEventIds.add(cEvent.getEventId());
                    globalEventIds.add(cEvent.getEventId());
                }
            }
            map.put(comp.getId(), cEventIds);
        }

        // из репоизтория event'ов вытаскиваем ВСЕ нужные ивенты
        Long[] globalEventIdsArray = globalEventIds.toArray(new Long[0]);
        List<Event> events = eventRepository.findAllByIdIn(globalEventIdsArray);

        // преобразуем event'ы в DTO
        List<EventShortDto> eventsForCompilationDtos = EventMapper.eventToShortDto(events);

        // обходим список Compilations и преобразуем в compilationDtoList
        for (Compilation comp : compilationList) {
            List<EventShortDto> subset = eventsForCompilationDtos
                    .stream()
                    .filter(c -> map.get(comp.getId()).contains(c.getId()))
                    .collect(Collectors.toList());
            CompilationDto el = CompMapper.compilationToDto(comp, subset);
            compilationDtoList.add(el);
        }
        return compilationDtoList;
    }
}
