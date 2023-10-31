package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import ru.practicum.comments.comments_models.CommentFullResponseDto;
import ru.practicum.comments.comments_models.CommentRequestDto;
import ru.practicum.comments.comments_models.CommentShortResponseDto;

import java.util.List;

public interface CommentService {

    CommentFullResponseDto createComment(Long userId, Long eventId, CommentRequestDto request, Long responseToId);

    CommentFullResponseDto updateCommentByAdmin(Long commentId, CommentRequestDto request);

    CommentFullResponseDto updateCommentByUser(Long userId, Long commentId, CommentRequestDto request);

    CommentFullResponseDto getCommentByAdmin(Long commentId);

    List<CommentShortResponseDto> getCommentsPublic(Long eventId, Integer from, Integer size);

    List<CommentFullResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size);

    CommentFullResponseDto getCommentByUser(Long userId, Long commentId);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);
}
