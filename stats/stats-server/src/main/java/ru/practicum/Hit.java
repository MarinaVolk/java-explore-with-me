package ru.practicum;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * File Name: ru.practicum.Hit.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:33 PM (UTC+3)
 * Description:
 */
@Entity
@Table(name = "stat")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String app;

    private String uri;

    private String ip;

    @Column(name = "created")
    private LocalDateTime timestamp;
}
