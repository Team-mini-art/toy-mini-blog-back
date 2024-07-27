package study.till.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.reply.ReplyRequest;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Reply;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.exception.member.NotMatchMemberException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.exception.reply.NotFoundReplyException;
import study.till.back.repository.CommentRepository;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;
import study.till.back.repository.ReplyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepositroy;
    private final ReplyRepository replyRepository;

    public ResponseEntity<CreateCommonResponse> createReply(ReplyRequest replyRequest) {
        Member member = memberRepository.findById(replyRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        Comment parentComment = commentRepositroy.findById(replyRequest.getPost_id()).orElseThrow(NotFoundCommentException::new);

        Reply reply = Reply.builder()
                .member(member)
                .parentComment(parentComment)
                .contents(replyRequest.getContents())
                .build();

        replyRepository.save(reply);

        CreateCommonResponse createCommonResponse = CreateCommonResponse.builder()
                .id(reply.getId())
                .status("SUCCESS")
                .message("대댓글 작성이 완료되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommonResponse);
    }

    public ResponseEntity<CommonResponse> updateReply(Long id, ReplyRequest replyRequest) {
        memberRepository.findById(replyRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        postRepository.findById(replyRequest.getPost_id()).orElseThrow(NotFoundPostException::new);

        Reply reply = replyRepository.findById(id).orElseThrow(NotFoundReplyException::new);

        commentRepositroy.findById(reply.getParentComment().getId()).orElseThrow(NotFoundCommentException::new);

        if (!reply.getMember().getEmail().equals(replyRequest.getEmail())) throw new NotMatchMemberException();

        reply.updateReply(replyRequest.getContents());
        replyRepository.save(reply);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("대댓글이 수정되었습니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }

    public ResponseEntity<CommonResponse> deleteReply(Long id) {
        replyRepository.findById(id).orElseThrow(NotFoundReplyException::new);

        replyRepository.deleteById(id);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("대댓글이 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(commonResponse);
    }
}
