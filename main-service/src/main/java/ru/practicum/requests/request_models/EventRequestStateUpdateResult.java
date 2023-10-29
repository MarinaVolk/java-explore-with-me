package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStateUpdateResult {
    private final List<RequestDto> confirmedRequests;

    private final List<RequestDto> rejectedRequests;
}
