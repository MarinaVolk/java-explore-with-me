package ru.practicum.events.event_models;/* # parse("File Header.java")*/

import lombok.*;
import ru.practicum.category.category_models.Category;
import ru.practicum.users.user_models.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * File Name: Event.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   6:59 PM (UTC+3)
 * Description:
 */

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false, unique = true)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Embedded
    private Location location;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    private String state;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "views")
    private Integer views;
}
