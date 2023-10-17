package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * File Name: ru.practicum.ViewStatsDto.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:46 PM (UTC+3)
 * Description:
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    private String app;

    private String uri;

    private Long hits;
}
