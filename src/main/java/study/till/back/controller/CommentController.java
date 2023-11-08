package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.comment.CommentRequest;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<FindCommentResponse>> findComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return commentService.findComments(pageable);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<FindCommentResponse> findComment(@PathVariable("id") Long id) {
        return commentService.findComment(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<CreateCommonResponse> createComment(@Valid @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(id, commentRequest);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable("id") Long id) {
        return commentService.deleteComment(id);
    }
}
