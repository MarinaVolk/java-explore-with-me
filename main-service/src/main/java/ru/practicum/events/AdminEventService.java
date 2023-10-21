package ru.practicum.events;/* # parse("File Header.java")*/

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.events.EventMapper.DATE_TIME_FORMATTER;

/**
 * File Name: AdminEventService.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:26 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    public List<EventFullDto> getByParams(Long[] users,
                                          String[] states,
                                          Long[] categories,
                                          String rangeStart,
                                          String rangeEnd,
                                          int from,
                                          int size) {

        PageRequest pageRequest;

        if (size > 0 && from >= 0) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        } else {
            throw new ValidationException("- Размер страницы должен быть > 0, 'from' должен быть >= 0");
        }




        BooleanExpression byUsers;
        if (users != null) {
            byUsers = QEvent.event.initiator.id.in(users);
        } else {
            byUsers = QEvent.event.id.isNotNull();
        }

        // статусы
        BooleanExpression byStates;
        if (states != null) {
            byStates = QEvent.event.state.in(states);
        } else {
            byStates = null;
        }

        // категории
        BooleanExpression byCategory;
        if (categories != null) {
            byCategory = QEvent.event.category.id.in(categories);
        } else {
            byCategory = null;
        }

        // блок старт
        BooleanExpression byStart;
        LocalDateTime rangeStartDate;
        if (rangeStart != null) {
            rangeStartDate = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        } else {
            rangeStartDate = LocalDateTime.now();
        }
        byStart = QEvent.event.eventDate.after(rangeStartDate);

        // блок энд
        BooleanExpression byEnd;
        if (rangeEnd != null) {
            byEnd = QEvent.event.eventDate.before(LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER));
        } else {
            byEnd = null;
        }




        // запрос в бд через QDSL
        Iterable<Event> foundEvents = eventRepository.findAll(byUsers

                        .and(byStates)
                        .and(byCategory)
                        .and(byStart)
                        .and(byEnd),

                pageRequest);

        // маппинг для возврата полученного списка
        List<EventFullDto> listToReturn = EventMapper.eventToFullDto(foundEvents);

        log.info("-- Список событий (Admin) возвращён, его размер: {}", listToReturn.size());

        return listToReturn;
    }

    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {

        log.info("-- Обновление события id={} (Admin): {}", eventId, updateRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        //блок проверок
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new NotAvailableException(
                    "- Событие не может быть раньше, чем через час от текущего момента ");
        }
        //конец блока проверок

        EventMapper.setIfNotNull(event::setAnnotation, updateRequest.getAnnotation());

        if (updateRequest.getCategory() != null) {
            Long categoryId = updateRequest.getCategory();
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                    new NotFoundException("- Категория с id=" + categoryId + " не найдена"));
            event.setCategory(category);
        }

        EventMapper.setIfNotNull(event::setDescription, updateRequest.getDescription());

        if (updateRequest.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(updateRequest.getEventDate(), DATE_TIME_FORMATTER);
            if (newEventDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("- Нельзя менять дату на более раннюю, чем текущее время");
            } else {
                event.setEventDate(newEventDate);
            }
        }

        EventMapper.setIfNotNull(event::setLocation, updateRequest.getLocation());
        EventMapper.setIfNotNull(event::setPaid, updateRequest.getPaid());
        EventMapper.setIfNotNull(event::setParticipantLimit, updateRequest.getParticipantLimit());
        EventMapper.setIfNotNull(event::setRequestModeration, updateRequest.getRequestModeration());

        if (updateRequest.getStateAction() != null) {

            if (updateRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)) {

                // Нельзя публиковать событие со статусом PUBLISHED или CANCELED
                EventState currentEventState = EventState.valueOf(event.getState());
                if (currentEventState.equals(EventState.PUBLISHED)
                        || currentEventState.equals(EventState.CANCELED)) {
                    throw new NotAvailableException("- Нельзя публиковать событие со статусом "
                            + currentEventState);

                } else {
                    event.setState(EventState.PUBLISHED.toString());
                    event.setPublishedOn(LocalDateTime.now());
                }

            } else if (updateRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT)) {

                // Нельзя отменять событие со статусом PUBLISHED
                EventState currentEventState = EventState.valueOf(event.getState());
                if (currentEventState.equals(EventState.PUBLISHED)) {
                    throw new NotAvailableException("- Нельзя отменять событие со статусом "
                            + currentEventState);

                } else {
                    event.setState(EventState.CANCELED.toString());
                }
            }
        }

        EventMapper.setIfNotNull(event::setTitle, updateRequest.getTitle());

        EventFullDto eventFullDto = EventMapper.eventToFullDto(eventRepository.save(event));

        return eventFullDto;
    }
}
