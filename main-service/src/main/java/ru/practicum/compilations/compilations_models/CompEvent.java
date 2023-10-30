package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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
