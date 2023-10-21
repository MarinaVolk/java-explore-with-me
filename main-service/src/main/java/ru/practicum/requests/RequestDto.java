package ru.practicum.requests;/* # parse("File Header.java")*/

import lombok.Data;

import java.time.LocalDateTime;

/**
 * File Name: RequestDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:43 PM (UTC+3)
 * Description:
 */

@Data
public class RequestDto {
    private final Long id;

    private final LocalDateTime created;

    private final Long event;

    private final Long requester;

    private final RequestState status;
}
