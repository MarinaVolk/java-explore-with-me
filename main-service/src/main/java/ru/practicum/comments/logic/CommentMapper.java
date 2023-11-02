package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import ru.practicum.comments.comments_models.Comment;
import ru.practicum.comments.comments_models.CommentFullResponseDto;
import ru.practicum.comments.comments_models.CommentShortResponseDto;


public class CommentMapper {

    public static CommentFullResponseDto toFullResponse(Comment comment) {
        return new CommentFullResponseDto(comment.getId(), comment.getText(), comment.getEvent().getId(), comment.getAuthor().getId(),
                comment.getCreated(), comment.getIsResponse(), comment.getParentCommentId());
    }

    public static CommentShortResponseDto toShortResponse(Comment comment) {
        return new CommentShortResponseDto(comment.getText(), comment.getAuthor().getId(), comment.getCreated());
    }
}
