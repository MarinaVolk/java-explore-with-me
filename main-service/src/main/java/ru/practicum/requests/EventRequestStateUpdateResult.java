package ru.practicum.requests;/* # parse("File Header.java")*/

import lombok.Data;

import java.util.List;

/**
 * File Name: EventRequestStateUpdateResult.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:43 PM (UTC+3)
 * Description:
 */

@Data
public class EventRequestStateUpdateResult {
    private final List<RequestDto> confirmedRequests;

    private final List<RequestDto> rejectedRequests;
}
