package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.comments_models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByAuthorId(Long userId, PageRequest page);

    Page<Comment> findByEventId(Long eventId, PageRequest page);

}
