package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.comment.CommentRequest;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.exception.member.NotMatchMemberException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.repository.CommentRepositroy;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
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

    @Transactional
    public ResponseEntity<CommonResponse> createComment(CommentRequest commentRequest) {
        Member member = memberRepository.findByEmail(commentRequest.getEmail());
        if (member == null) throw new NotFoundMemberException();
        
        Post post = postRepository.findById(commentRequest.getPost_id()).orElse(null);
        if (post == null) throw new NotFoundPostException();
        
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .contents(commentRequest.getContents())
                .build();
        
        commentRepositroy.save(comment);
        
        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("댓글 작성이 완료되었습니다.")
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> updateComment(Long id, CommentRequest commentRequest) {
        Member member = memberRepository.findByEmail(commentRequest.getEmail());
        if (member == null) throw new NotFoundMemberException();

        Post post = postRepository.findById(commentRequest.getPost_id()).orElse(null);
        if (post == null) throw new NotFoundPostException();

        Comment comment = commentRepositroy.findById(id).orElse(null);
        if (comment == null) throw new NotFoundCommentException();

        comment.updateComment(commentRequest.getContents());
        commentRepositroy.save(comment);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("댓글이 수정되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> deleteComment(Long id) {
        Comment comment = commentRepositroy.findById(id).orElse(null);
        if (comment == null) throw new NotFoundCommentException();

        commentRepositroy.deleteById(id);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESSS")
                .message("댓글이 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}
