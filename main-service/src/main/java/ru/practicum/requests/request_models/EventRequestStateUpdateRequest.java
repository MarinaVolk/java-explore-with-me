package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStateUpdateRequest {
    private List<Long> requestIds;

    private RequestUpdateState status;
}
