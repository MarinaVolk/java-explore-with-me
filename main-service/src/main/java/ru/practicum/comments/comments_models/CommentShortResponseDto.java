package ru.practicum.comments.comments_models;/* # parse("File Header.java")*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortResponseDto {
    private String text;
    private Long userId;
    private LocalDateTime created;
}