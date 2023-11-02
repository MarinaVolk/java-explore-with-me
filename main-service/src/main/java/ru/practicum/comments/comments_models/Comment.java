package ru.practicum.comments.comments_models;/* # parse("File Header.java")*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.events.event_models.Event;
import ru.practicum.users.user_models.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "author_id", updatable = false)
    private User author;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "is_response")
    private Boolean isResponse;
    @Column(name = "parent_comment_id")
    private Long parentCommentId;
}
