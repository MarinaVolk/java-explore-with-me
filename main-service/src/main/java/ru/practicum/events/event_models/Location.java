package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@RequiredArgsConstructor
public class Location {
    private double lat;

    private double lon;
}
