package study.till.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.comment.CommentRequest;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.common.NoDataException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.exception.member.NotMatchMemberException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.repository.CommentRepositroy;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepositroy commentRepositroy;

    public ResponseEntity<List<FindCommentResponse>> findComments(Pageable pageable) {
        Page<Comment> comments = commentRepositroy.findAll(pageable);

        if (comments.isEmpty()) {
            throw new NoDataException();
        }

        List<FindCommentResponse> findCommentResponses = comments.stream()
                .map(FindCommentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(findCommentResponses);
    }


    public ResponseEntity<FindCommentResponse> findComment(Long id) {
        Comment comment = commentRepositroy.findById(id).orElseThrow(NotFoundCommentException::new);

        FindCommentResponse findCommentResponse = FindCommentResponse.from(comment);

        return ResponseEntity.ok(findCommentResponse);
    }

    @Transactional
    public ResponseEntity<CreateCommonResponse> createComment(CommentRequest commentRequest) {
        Member member = memberRepository.findById(commentRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        Post post = postRepository.findById(commentRequest.getPost_id()).orElseThrow(NotFoundPostException::new);

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .contents(commentRequest.getContents())
                .build();

        commentRepositroy.save(comment);

        CreateCommonResponse createCommonResponse = CreateCommonResponse.builder()
                .id(comment.getId())
                .status("SUCCESS")
                .message("댓글 작성이 완료되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommonResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> updateComment(Long id, CommentRequest commentRequest) {
        Member member = memberRepository.findById(commentRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        postRepository.findById(commentRequest.getPost_id()).orElseThrow(NotFoundPostException::new);

        Comment comment = commentRepositroy.findById(id).orElseThrow(NotFoundCommentException::new);

        if (!member.getEmail().equals(comment.getMember().getEmail())) throw new NotMatchMemberException();

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
        commentRepositroy.findById(id).orElseThrow(NotFoundCommentException::new);

        commentRepositroy.deleteById(id);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("댓글이 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}
