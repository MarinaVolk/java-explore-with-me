package ru.practicum.events;/* # parse("File Header.java")*/

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

/**
 * File Name: Location.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:11 PM (UTC+3)
 * Description:
 */

@Embeddable
@Data
@RequiredArgsConstructor
public class Location {
    private double lat;

    private double lon;
}
