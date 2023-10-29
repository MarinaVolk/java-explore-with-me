package ru.practicum.events.event_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.category_models.Category;
import ru.practicum.category.logic.CategoryRepository;
import ru.practicum.events.event_models.*;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.request_logic.RequestRepository;
import ru.practicum.requests.request_models.*;
import ru.practicum.users.user_models.User;
import ru.practicum.users.user_logic.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.events.event_models.EventMapper.DATE_TIME_FORMATTER;


@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;


    @Transactional
    public EventFullDto add(Long initiatorId, NewEventDto newEventDto) {

        log.info("-- Добавление события от пользователя id={}: {}", initiatorId, newEventDto);

        //блок проверок
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), DATE_TIME_FORMATTER);

        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("- Время события должно быть позже текущего");
        }
        // конец блока проверок

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new NotFoundException("- Категория №" + newEventDto.getCategory() + " не найдена в базе"));

        User initiator = userRepository.findById(initiatorId).orElseThrow(() ->
                new NotFoundException("- Пользователь с id=" + initiatorId + " не найден"));

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

        log.info("-- Событие от пользователя id={} добавлено: {}", initiatorId, eventFullDto);

        return eventFullDto;
    }

    public List<EventShortDto> getByInitiatorId(Long initiatorId, int from, int size) {

        log.info("-- Возвращение всех событий от пользователя id={}", initiatorId);

        PageRequest pageRequest;

        if (size > 0 && from >= 0) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        } else {
            throw new ValidationException("- Размер страницы должен быть > 0, 'from' должен быть >= 0");
        }

        List<EventShortDto> listToReturn = EventMapper.eventToShortDto(
                eventRepository.findByInitiatorId(initiatorId, pageRequest));

        log.info("-- Список событий от пользователя id={} возвращён, его размер: {}", initiatorId, listToReturn.size());

        return listToReturn;
    }

    public EventFullDto getById(Long initiatorId, Long eventId) {

        log.info("-- Возвращение события id={} от пользователя id={}", eventId, initiatorId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        //блок проверок
        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new ValidationException("- Пользователь не является инициатором события id=" + eventId);
        }
        //конец блока проверок

        EventFullDto eventFullDto = EventMapper.eventToFullDto(event);

        log.info("-- Событие с id={} возвращёно", eventId);

        return eventFullDto;
    }

    @Transactional
    public EventFullDto updateEventByInitiator(Long initiatorId, Long eventId, UpdateEventUserRequest updateRequest) {

        log.info("-- Обновление события id={} от пользователя c id={}: {}", eventId, initiatorId, updateRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        //блок проверок
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

    public List<RequestDto> getRequests(Long initiatorId, Long eventId) {

        log.info("-- Возвращение списка запросов на участие в событии id={}", eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        //блок проверок
        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new ValidationException("- Пользователь не является инициатором события id=" + eventId);
        }
        //конец блока проверок

        List<RequestDto> listToReturn = requestRepository.findByEvent(eventId)
                .stream()
                .map(RequestMapper::partRequestToDto)
                .collect(Collectors.toList());

        log.info("-- Список запросов на участие в событи id={} возвращён, его размер: {}",
                eventId, listToReturn.size());

        return listToReturn;
    }

    @Transactional
    public EventRequestStateUpdateResult updateRequestStatusFromInitiator(

            Long initiatorId,
            Long eventId,
            EventRequestStateUpdateRequest statusUpdateRequest) {

        log.info("-- Обновление запросов на участие в событии c id={} от пользователя с id={}: {}",
                eventId, initiatorId, statusUpdateRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("- Событие с id=" + eventId + " не найдено"));

        Integer numberOfConfirmedRequests;
        if (event.getConfirmedRequests() != null) {
            numberOfConfirmedRequests = event.getConfirmedRequests();
        } else {
            numberOfConfirmedRequests = 0;
        }

        //блок проверок
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ValidationException("- Подтверждение заявок не требуется событию с id=" + eventId);
        }

        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new ValidationException("- Пользователь не является инициатором события id=" + eventId);
        }

        if (numberOfConfirmedRequests >= event.getParticipantLimit()) {
            throw new NotAvailableException("- Достигнут лимит запросов на участие в событии");
        }
        //конец блока проверок

        List<Long> ids = statusUpdateRequest.getRequestIds();
        RequestUpdateState stateToSet = statusUpdateRequest.getStatus();

        List<Request> requests = requestRepository.findByIdIn(ids);
        List<Long> confirmedIds = new ArrayList<>();
        List<Long> rejectedIds = new ArrayList<>();

        for (Request request : requests) {

            Long requestId = request.getId();

            if (!request.getEvent().equals(eventId)) {
                log.info("- Запрос на участие с id={} не относится к собыию с id={}",
                        requestId, eventId);
                continue;
            }

            if (!request.getStatus().equals(RequestState.PENDING.toString())) {
                log.info("- Запрос на участие с id={} не имеет статус PENDING",
                        requestId);
            }

            if (numberOfConfirmedRequests >= event.getParticipantLimit()
                    || stateToSet.equals(RequestUpdateState.REJECTED)) {

                request.setStatus(RequestState.REJECTED.toString());
                rejectedIds.add(requestId);
                continue;
            }

            request.setStatus(RequestState.CONFIRMED.toString());
            confirmedIds.add(requestId);
            numberOfConfirmedRequests++;
        }

        requestRepository.setStatus(confirmedIds, RequestState.CONFIRMED.toString());
        requestRepository.setStatus(rejectedIds, RequestState.REJECTED.toString());

        // меняем число подтвержденных запросов события
        event.setConfirmedRequests(numberOfConfirmedRequests);
        eventRepository.save(event);

        log.info("--- Число подтвержденных запросов на участие в событии с id={} обновлено: {}",
                eventId, numberOfConfirmedRequests);

        // получаем список запросов с id из [ids], которые относятся к данному событию
        List<Request> listOfProperRequests =
                requestRepository.findByEventAndIdIn(eventId, ids);

        EventRequestStateUpdateResult result = new EventRequestStateUpdateResult(

                // получаем список подтвержденных запросов
                RequestMapper.partRequestToDto(
                        listOfProperRequests.stream()
                                .filter(p -> p.getStatus().equals(RequestState.CONFIRMED.toString()))
                                .collect(Collectors.toList())),

                // получаем список отклонённых запросов
                RequestMapper.partRequestToDto(
                        listOfProperRequests.stream()
                                .filter(p -> p.getStatus().equals(RequestState.REJECTED.toString()))
                                .collect(Collectors.toList()))
        );

        log.info("-- Статусы запросов на участие в событии id={} обновлены, одобрено: {}, отклонено: {}",
                eventId, result.getConfirmedRequests().size(), result.getRejectedRequests().size());

        return result;
    }
}
