package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private final Long id;

    private final LocalDateTime created;

    private final Long event;

    private final Long requester;

    private final RequestState status;
}
