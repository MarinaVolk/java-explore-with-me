package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.comments_models.CommentFullResponseDto;
import ru.practicum.comments.comments_models.CommentRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Valid
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService service) {
        this.commentService = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullResponseDto createComment(@RequestBody @Valid CommentRequestDto request,
                                                @PathVariable(name = "userId") @Min(1) Long userId,
                                                @RequestParam(name = "eventId") @Min(1) Long eventId,
                                                @RequestParam(name = "responseToId", required = false)
                                                @Min(1) Long responseToId) {
        return commentService.createComment(userId, eventId, request, responseToId);
    }

    @GetMapping
    public List<CommentFullResponseDto> getAllCommentsByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                                             @RequestParam(name = "from", defaultValue = "0")
                                                             @Min(0) Integer from,
                                                             @RequestParam(name = "size", defaultValue = "10")
                                                             @Min(1) Integer size) {
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentFullResponseDto getCommentByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                                   @PathVariable(name = "commentId") @Min(1) Long commentId) {
        return commentService.getCommentByUser(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullResponseDto updateCommentByUser(@RequestBody @Valid CommentRequestDto request,
                                                      @PathVariable(name = "userId") @Min(1) Long userId,
                                                      @PathVariable(name = "commentId") @Min(1) Long commentId) {
        return commentService.updateCommentByUser(userId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByUser(@PathVariable(name = "userId") @Min(1) Long userId,
                                    @PathVariable(name = "commentId") @Min(1) Long commentId) {
        commentService.deleteCommentByUser(userId, commentId);
    }
}
