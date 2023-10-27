package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * File Name: CompEvent.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:28 PM (UTC+3)
 * Description:
 */

@Entity
@Table(name = "comp_events")
@Getter
@Setter
@ToString
public class CompEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long compId;

    private Long eventId;
}
