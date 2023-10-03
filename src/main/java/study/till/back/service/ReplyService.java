package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.reply.ReplyRequest;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Reply;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.repository.CommentRepositroy;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.ReplyRepository;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final MemberRepository memberRepository;
    private final CommentRepositroy commentRepositroy;
    private final ReplyRepository replyRepository;

    public ResponseEntity<CreateCommonResponse> createReplyComment(ReplyRequest replyRequest) {
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
}
