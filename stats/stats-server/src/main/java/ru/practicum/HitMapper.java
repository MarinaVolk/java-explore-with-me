package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * File Name: ru.practicum.HitMapper.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:36 PM (UTC+3)
 * Description:
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {
    public static Hit statsHitDtoToStatHit(HitDto statsHitDto) {
        return Hit.builder()
                .app(statsHitDto.getApp())
                .uri(statsHitDto.getUri())
                .ip(statsHitDto.getIp())
                .timestamp(statsHitDto.getTimestamp())
                .build();
    }
}
