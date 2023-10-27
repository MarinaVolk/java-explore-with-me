package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * File Name: Request.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:38 PM (UTC+3)
 * Description:
 */

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "event", nullable = false)
    private Long event;

    @Column(name = "requester", nullable = false)
    private Long requester;

    @Column(name = "status")
    private String status;
}
