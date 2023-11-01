package ru.practicum.comments.logic;/* # parse("File Header.java")*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.comments_models.CommentFullResponseDto;
import ru.practicum.comments.comments_models.CommentRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService service) {
        this.commentService = service;
    }

    @GetMapping("/{commentId}")
    public CommentFullResponseDto getCommentByAdmin(@PathVariable(name = "commentId") @Min(1) Long commentId) {
        return commentService.getCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullResponseDto updateCommentByAdmin(@RequestBody @Valid CommentRequestDto request,
                                                       @PathVariable(name = "commentId") @Min(1) Long commentId) {
        return commentService.updateCommentByAdmin(commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable(name = "commentId") @Min(1) Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
