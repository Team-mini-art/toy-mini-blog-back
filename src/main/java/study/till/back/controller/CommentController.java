package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.comment.CommentRequest;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<FindCommentResponse>> findComments() {
        return commentService.findComments();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<FindCommentResponse> findComment(@PathVariable("id") Long id) {
        return commentService.findComment(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<CommonResponse> createComment(@RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(id, commentRequest);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("id") Long id) {
        return commentService.deleteComment(id);
    }
}
