package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import lombok.Data;
import ru.practicum.requests.request_models.RequestUpdateState;

import java.util.List;

/**
 * File Name: EventRequestStateUpdateRequest.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:42 PM (UTC+3)
 * Description:
 */

@Data
public class EventRequestStateUpdateRequest {
    private List<Long> requestIds;

    private RequestUpdateState status;
}
