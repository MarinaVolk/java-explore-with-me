package ru.practicum.events;/* # parse("File Header.java")*/

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.events.EventMapper.DATE_TIME_FORMATTER;

/**
 * File Name: PublicEventService.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:29 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {
    @Value("${app-name}")
    private String appName;

    private final StatsClient statClientService;

    private final EventRepository eventRepository;

    public List<EventShortDto> getByParameters(String uri, String ip,

                                               String text,
                                               Long[] categories,
                                               Boolean paid,
                                               String rangeStart,
                                               String rangeEnd,
                                               Boolean onlyAvailable,
                                               String sort,
                                               int from,
                                               int size) {

        log.info("-- Возвращение событий с параметрами (Public): " +
                        "text={}, categories={}, paid={}, start={}, end={}, onlyAvailable={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);

        // блок проверок:

        // start end
        LocalDateTime rangeStartDate;
        LocalDateTime rangeEndDate;
        if (rangeStart != null && rangeEnd != null) {
            rangeStartDate = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
            rangeEndDate = LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER);
            if (rangeEndDate.isBefore(rangeStartDate)) {
                throw new ValidationException("- start должен быть раньше end");
            }
        }

        // sort
        if (sort.equalsIgnoreCase("EVENT_DATE")) {
            sort = "eventDate";
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            sort = "views";
        } else {
            throw new ValidationException("- sort должен быть EVENT_DATE или VIEWS");
        }
        // конец блока проверок

        // отправляем запись о запросе в сервис статистики
        LocalDateTime timestamp = LocalDateTime.now();
        statClientService.saveStats(appName, uri, ip, timestamp);

        // блок пагинации
        PageRequest pageRequest;

        if (size > 0 && from >= 0) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            throw new ValidationException("- Размер страницы должен быть > 0, 'from' должен быть >= 0");
        }

        // Блок проверки опубликованности
        BooleanExpression byState = QEvent.event.state.eq(EventState.PUBLISHED.toString());

        // Блок поиска:

        // text
        BooleanExpression byText;
        if (text != null) {
            byText = QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text));
        } else {
            byText = null;
        }

        // категории
        BooleanExpression byCategory;
        if (categories != null) {
            byCategory = QEvent.event.category.id.in(categories);
        } else {
            byCategory = null;
        }

        // платность
        BooleanExpression byPaid;
        if (paid != null) {
            byPaid = QEvent.event.paid.eq(paid);
        } else {
            byPaid = null;
        }

        // старт
        BooleanExpression byStart;
        if (rangeStart != null) {
            rangeStartDate = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        } else {
            rangeStartDate = LocalDateTime.now();
        }
        byStart = QEvent.event.eventDate.after(rangeStartDate);

        // энд
        BooleanExpression byEnd;
        if (rangeEnd != null) {
            byEnd = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER));
        } else {
            byEnd = null;
        }

        // доступность
        BooleanExpression byAvailable;
        if (onlyAvailable != null && onlyAvailable) {
            byAvailable = QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit);
        } else {
            byAvailable = null;
        }

        // запрос в бд через QDSL
        Iterable<Event> foundEvents = eventRepository.findAll(byState

                        .and(byText)
                        .and(byCategory)
                        .and(byPaid)
                        .and(byStart)
                        .and(byEnd)
                        .and(byAvailable),

                pageRequest);

        // маппинг для возврата полученного списка
        List<EventShortDto> listToReturn = EventMapper.eventToShortDto(foundEvents);

        log.info("-- Список событий (Public) возвращен, его размер: {}", listToReturn.size());

        return listToReturn;
    }

    @Transactional
    public EventFullDto getById(Long eventId, String uri, String ip) {

        log.info("-- Возвращение события id={} (Public)", eventId);

        // отправляем запись о запросе в сервис статистики
        LocalDateTime timestamp = LocalDateTime.now();
        statClientService.saveStats(appName, uri, ip, timestamp);

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED.toString()).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено или не опубликовано (Public)"));

        // отметка просмотров события в бд
        /*String start = LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String[] uris = {uri};
        Integer views = statClientService.getStats(start, end, uris, true);
        event.setViews(views); */

        EventFullDto eventFullDto = EventMapper.eventToFullDto(eventRepository.save(event)); //event);

        log.info("-- Событие с id={} возвращёно (Public)", eventId);

        return eventFullDto;
    }

}
