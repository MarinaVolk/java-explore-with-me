package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.category.category_models.Category;
import ru.practicum.category.logic.CategoryRepository;
import ru.practicum.events.event_models.*;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.users.user_models.User;
import ru.practicum.users.user_logic.UserRepository;
import ru.practicum.util.Sorts;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.events.event_models.EventMapper.DATE_TIME_FORMATTER;


@Service
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {
    private final StatsClient client;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public static final String START_TIME = "2020-01-01 00:00:00";
    public static final String END_TIME = "2300-01-01 00:00:00";

    @Autowired
    public EventServiceImpl(StatsClient client, EventRepository eventRep, CategoryRepository catRep,
                            UserRepository userRep) {
        this.client = client;
        this.eventRepository = eventRep;
        this.categoryRepository = catRep;
        this.userRepository = userRep;
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size,
                                                         HttpServletRequest request) {
        final List<EventShortDto> response = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size))
                .stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
        saveHit(request);
        return response;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {

        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), DATE_TIME_FORMATTER);

        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("- Время события должно быть позже текущего");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("- Категория №" + newEventDto.getCategory() + " не найдена в базе"));

        User initiator = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- Пользователь с id=" + userId + " не найден"));

        Event event = EventMapper.newEventToModel(newEventDto, category, initiator);

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }

        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }

        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING.toString());
        event.setViews(0);

        EventFullDto eventFullDto = EventMapper.eventToFullDto(eventRepository.save(event));

        log.info("-- Событие от пользователя id={} добавлено: {}", userId, eventFullDto);

        return eventFullDto;
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId, HttpServletRequest request) {
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", eventId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException(String.format(
                    "User с id: %d не является инициатором события, полный просмотр недоступен", eventId));
        }
        saveHit(request);
        event.setViews(getViews(request));
        final EventFullDto response = EventMapper.eventToFullDto(eventRepository.save(event));
        log.debug("В БД сохранено новое значение views для event: {}", response);
        return response;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(UpdateEventUserRequest updateRequest, Long initiatorId, Long eventId) {
        log.info("-- Обновление события id={} от пользователя c id={}: {}", eventId, initiatorId, updateRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new ValidationException("- Пользователь не является инициатором события id=" + eventId);
        }

        if (event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new NotAvailableException("- Нельзя изенять событие со статусом " + EventState.PUBLISHED);
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new NotAvailableException(
                    "- Событие не может быть раньше, чем через два часа от текущего момента ");
        }

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
            UpdateEventUserRequest.StateAction stateAction = updateRequest.getStateAction();
            if (stateAction.equals(UpdateEventUserRequest.StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING.toString());
            } else if (stateAction.equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED.toString());
            }
        }

        EventMapper.setIfNotNull(event::setTitle, updateRequest.getTitle());

        EventFullDto eventFullDto = EventMapper.eventToFullDto(eventRepository.save(event));

        log.info("-- Событие id={} от пользователя c id={} обновлено: {}", eventId, initiatorId, eventFullDto);

        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getAdminEventsByParams(Long[] users,
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

        // start
        BooleanExpression byStart;
        LocalDateTime rangeStartDate;
        if (rangeStart != null) {
            rangeStartDate = LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER);
        } else {
            rangeStartDate = LocalDateTime.now();
        }
        byStart = QEvent.event.eventDate.after(rangeStartDate);

        // end
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

    @Override
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

    @Override
    public List<EventShortDto> getPublicEventsByParams(EventPublicParams params, Integer from, Integer size,
                                                               HttpServletRequest request) {
        final QEvent EVENT = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder(EVENT.isNotNull());
        if (params.getText() != null && !params.getText().isBlank()) {
            builder.and(EVENT.annotation.containsIgnoreCase(params.getText())
                    .or(EVENT.description.containsIgnoreCase(params.getText())));
        }
        if (params.getCategories() != null && !params.getCategories().isEmpty() && params.getCategories().get(0) != 0) {
            builder.and(EVENT.category.id.in(params.getCategories()));
        }
        if (params.getPaid() != null) {
            builder.and(EVENT.paid.eq(params.getPaid()));
        }
        if (params.getRangeStart() == null && params.getRangeEnd() == null) {
            builder.and(EVENT.eventDate.after(LocalDateTime.now()));
        } else if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new ValidationException("rangeEnd не может быть раньше rangeStart");
            }
            builder.and(EVENT.eventDate.between(params.getRangeStart(), params.getRangeEnd()));
        } else if (params.getRangeEnd() != null) {
            builder.and(EVENT.eventDate.before(params.getRangeEnd()));
        } else {
            builder.and(EVENT.eventDate.after(params.getRangeStart()));
        }
        if (params.getOnlyAvailable() != null) {
            if (params.getOnlyAvailable()) {
                builder.and(EVENT.participantLimit.eq(0).or(EVENT.participantLimit.goe(EVENT.confirmedRequests)));
            }
        }
        builder.and(EVENT.state.eq(String.valueOf(EventState.PUBLISHED)));
        if (params.getSort() != null) {
            if (params.getSort() == Sorts.EVENT_DATE) {
                final List<EventShortDto> response = eventRepository.findAll(builder,
                                PageRequest.of(from / size, size, Sort.Direction.ASC, "eventDate"))
                        .stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
                saveHit(request);
                return response;
            } else if (params.getSort() == Sorts.VIEWS) {
                final List<EventShortDto> response = eventRepository.findAll(builder,
                                PageRequest.of(from / size, size, Sort.Direction.DESC, "views"))
                        .stream().map(EventMapper::eventToShortDto).collect(Collectors.toList());
                saveHit(request);
                return response;
            }
        }
        final List<EventShortDto> response = eventRepository.findAll(builder, PageRequest.of(from / size, size)).stream().map(EventMapper::eventToShortDto)
                .collect(Collectors.toList());
        saveHit(request);
        return response;
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        final Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("В базе данных нет события с id - %s", id)));
        if (!Objects.equals(event.getState(), EventState.PUBLISHED.toString())) {
            throw new NotFoundException(String.format("Событие не найдено или недоступно, eventId: %s", id));
        }
        saveHit(request);
        event.setViews(getViews(request));
        final EventFullDto response = EventMapper.eventToFullDto(eventRepository.save(event));
        log.debug("В БД сохранено новое значение views для event: {}", response);
        return response;
    }

    private void saveHit(HttpServletRequest request) {
        client.saveStats("emw-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now());
    }

    private void dateValidation(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now().withNano(0).plusHours(2))) {
            throw new ValidationException(
                    String.format("Дата начала события должно быть позже на 2 часа создания события, today: %s, eventDate: %s",
                            LocalDateTime.now(), date));
        }
    }

    private LocalDateTime parseDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    private Integer getViews(HttpServletRequest request) {
        List<ViewStatsDto> stats = client.getStats(START_TIME, END_TIME, request.getRequestURI(),
                true).getBody();
        Integer views = 0;
        if (stats != null) {
            views = stats.stream().filter(e -> e.getUri().equals(request.getRequestURI())).findFirst().get().getHits().intValue(); // !!!!!! Int
        }
        return views;
    }

}
