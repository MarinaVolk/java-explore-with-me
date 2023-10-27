package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * File Name: Compilation.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:01 PM (UTC+3)
 * Description:
 */

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    @Column(name = "title", nullable = false, unique = true)
    private String title;
}
