package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.comments_models.Comment;
import ru.practicum.comments.comments_models.CommentFullResponseDto;
import ru.practicum.comments.comments_models.CommentRequestDto;
import ru.practicum.comments.comments_models.CommentShortResponseDto;
import ru.practicum.events.event_logic.EventRepository;
import ru.practicum.events.event_models.Event;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.users.user_logic.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository comRep, UserRepository userRep, EventRepository eventRep) {
        this.commentRepository = comRep;
        this.userRepository = userRep;
        this.eventRepository = eventRep;
    }

    @Override
    public CommentFullResponseDto getCommentByAdmin(Long commentId) {
        return CommentMapper.toFullResponse(commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Отсутствует комментарий с id %s", commentId))));
    }

    @Override
    public CommentFullResponseDto updateCommentByAdmin(Long commentId, CommentRequestDto request) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Отсутствует комментарий с id %s", commentId)));
        comment.setText(request.getText());
        final Comment updComment = commentRepository.save(comment);
        log.debug("Комментарий успешно обновлен: {}", updComment);
        return CommentMapper.toFullResponse(updComment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new NotFoundException(String.format("В базе данных нет комментария с id %s", commentId));
        }
    }

    @Override
    public CommentFullResponseDto createComment(Long userId, Long eventId, CommentRequestDto request, Long responseToId) {
        userValidate(userId);
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(
                "В базе данных отсутствует событие с id - %d", eventId)));
        /*if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Комментарий не может быть добавлен для неопубликованного события.");
        }*/
        final Comment comment = commentRepository.save(new Comment(null, request.getText(), eventId, userId, LocalDateTime.now(),
                false, List.of()));
        if (responseToId != null) {
            final Comment mainCom = commentRepository.findById(responseToId).orElseThrow(() -> new NotFoundException(
                    String.format("В базе данных отсутствует комментарий с id  %d", responseToId)));
            if (!mainCom.getEventId().equals(eventId)) {
                throw new ValidationException(String.format("Ответ на комментарий должен " +
                                "быть с тем же eventId что и основной комментарий: expected - %s, current - %s",
                        comment.getEventId(), eventId));
            }
            comment.setIsResponse(true);
            mainCom.getResponses().add(comment);
            commentRepository.save(mainCom);
        }
        return CommentMapper.toFullResponse(comment);
    }

    @Override
    public List<CommentFullResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        userValidate(userId);
        return commentRepository.findByUserId(userId, PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toFullResponse).collect(Collectors.toList());
    }

    @Override
    public CommentFullResponseDto getCommentByUser(Long userId, Long commentId) {
        final Comment comment = commentValidate(userId, commentId);
        return CommentMapper.toFullResponse(comment);
    }

    @Override
    public CommentFullResponseDto updateCommentByUser(Long userId, Long commentId, CommentRequestDto request) {
        final Comment comment = commentValidate(userId, commentId);
        comment.setText(request.getText());
        final Comment updComment = commentRepository.save(comment);
        log.debug("Комментарий успешно обновлен: {}", updComment);
        return CommentMapper.toFullResponse(updComment);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        final Comment comment = commentValidate(userId, commentId);
        if (!comment.getResponses().isEmpty()) {
            for (Comment currentComment : comment.getResponses()) {
                currentComment.setIsResponse(false);
                commentRepository.save(currentComment);
            }
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentShortResponseDto> getCommentsPublic(Long eventId, Integer from, Integer size) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Отсутствует событие с id %d", eventId));
        }
        return commentRepository.findByEventId(eventId, PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toShortResponse).collect(Collectors.toList());
    }

    private void userValidate(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Отсутствует пользователь с id %d", userId));
        }
    }

    private Comment commentValidate(Long userId, Long commentId) {
        userValidate(userId);
        final Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                String.format("Отсутствует комментарий с id %d", commentId)));
        if (!comment.getUserId().equals(userId)) {
            throw new ValidationException(String.format("Невозможно получить полную информацию о комментарии - " +
                    "пользователь с id: %d не является автором комментария с id: %d", userId, commentId));
        }
        return comment;
    }

}