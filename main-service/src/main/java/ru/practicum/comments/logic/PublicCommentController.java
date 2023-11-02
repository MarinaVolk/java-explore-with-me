package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.comments_models.CommentShortResponseDto;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/events/{eventId}/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @Autowired
    public PublicCommentController(CommentService service) {
        this.commentService = service;
    }

    @GetMapping
    public List<CommentShortResponseDto> getCommentsPublic(@PathVariable(name = "eventId") @Min(1) Long eventId,
                                                           @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        return commentService.getCommentsPublic(eventId, from, size);
    }
}
