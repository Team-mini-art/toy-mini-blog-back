package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.entity.Comment;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.repository.CommentRepositroy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepositroy commentRepositroy;

    public ResponseEntity<List<FindCommentResponse>> findComments() {
        List<Comment> comments = commentRepositroy.findAll();

        List<FindCommentResponse> findCommentResponse = comments.stream().map(comment -> FindCommentResponse.builder()
                .id(comment.getId())
                .email(comment.getMember().getEmail())
                .nickname(comment.getMember().getNickname())
                .post_id(comment.getPost().getId())
                .contents(comment.getContents())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(findCommentResponse);
    }

    public ResponseEntity<FindCommentResponse> findComment(Long id) {
        Comment comment = commentRepositroy.findById(id).orElse(null);

        if (comment == null) throw new NotFoundCommentException();

        FindCommentResponse findCommentResponse = FindCommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost().getId())
                .email(comment.getMember().getEmail())
                .nickname(comment.getMember().getNickname())
                .contents(comment.getContents())
                .build();

        return ResponseEntity.ok(findCommentResponse);
    }
}
