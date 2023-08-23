package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/comment/{id}")
    public ResponseEntity<FindCommentResponse> findComment(@PathVariable("id") Long id) {
        return commentService.findComment(id);
    }
}
