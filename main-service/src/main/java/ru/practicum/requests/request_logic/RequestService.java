package ru.practicum.requests.request_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.events.event_models.Event;
import ru.practicum.events.event_models.EventMapper;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.events.event_models.EventState;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.request_models.Request;
import ru.practicum.requests.request_models.RequestDto;
import ru.practicum.requests.request_models.RequestMapper;
import ru.practicum.requests.request_models.RequestState;
import ru.practicum.users.user_logic.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File Name: RequestService.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:47 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Transactional
    public RequestDto add(Long requesterId, Long eventId) {

        String currentTimeString = LocalDateTime.now().format(EventMapper.DATE_TIME_FORMATTER);

        if (!userRepository.existsById(requesterId)) {
            throw new NotFoundException("Пользователь с id: " + requesterId + " не найден");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id: " + eventId + " не найдено"));

        if (requestRepository.existsByRequesterAndEvent(requesterId, eventId)) {
            throw new NotAvailableException("Запрос пользователя id: " + requesterId +
                    " на участие в этом событии уже существует");
        }

        if (event.getInitiator().getId().equals(requesterId)) {
            throw new NotAvailableException("Пользователь не может создавать запрос на участие " +
                    "в своем событии");
        }

        if (!event.getState().equals(EventState.PUBLISHED.toString())) {
            throw new NotAvailableException("Нельзя участвовать в неопубликованном событии");
        }

        Integer confRequests = event.getConfirmedRequests();
        Integer partLimit = event.getParticipantLimit();

        if (confRequests != null && partLimit != null && partLimit != 0
                && confRequests >= partLimit) {

            throw new NotAvailableException("Достигнут лимит запросов на участие в событии");
        }

        Request participationRequest = new Request();

        participationRequest.setCreated(LocalDateTime.parse(currentTimeString, EventMapper.DATE_TIME_FORMATTER));
        participationRequest.setEvent(eventId);
        participationRequest.setRequester(requesterId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestState.CONFIRMED.toString());
            eventRepository.updateConfirmedRequestsById(eventId);
        } else {
            participationRequest.setStatus(RequestState.PENDING.toString());
        }

        RequestDto partRequestDto =
                RequestMapper.partRequestToDto(requestRepository.save(participationRequest));

        return partRequestDto;
    }

    public List<RequestDto> getByRequesterId(Long requesterId) {

        if (!userRepository.existsById(requesterId)) {
            throw new NotFoundException("Пользователь с id: " + requesterId + " не найден");
        }

        List<RequestDto> listToReturn = requestRepository.findByRequester(requesterId)
                .stream()
                .map(RequestMapper::partRequestToDto)
                .collect(Collectors.toList());

        return listToReturn;
    }

    @Transactional
    public RequestDto cancelPartRequest(Long requesterId, Long requestId) {

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id: " + requestId + " не найден"));

        if (!userRepository.existsById(requesterId)) {
            throw new NotFoundException("Пользователь с id: " + requesterId + " не найден");
        }

        if (!request.getRequester().equals(requesterId)) {
            throw new ValidationException("Пользователь с id: " + requesterId +
                    " не создавал запрос c id=" + requestId);
        }

        request.setStatus(RequestState.CANCELED.toString());

        RequestDto requestDto = RequestMapper.partRequestToDto(requestRepository.save(request));
        return requestDto;
    }
}
