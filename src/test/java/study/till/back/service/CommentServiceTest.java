package study.till.back.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.till.back.dto.comment.FindCommentResponse;
import study.till.back.dto.reply.ReplyDTO;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.entity.Reply;
import study.till.back.repository.CommentRepositroy;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;
import study.till.back.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepositroy commentRepositroy;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @Transactional
    void findCommentsTest() {
        Member member1 = Member.builder()
                .email("a@a.com")
                .nickname("a")
                .password("1234")
                .build();

        Post post1 = Post.builder()
                .id(1)
                .member(member1)
                .title("test1")
                .contents("test1")
                .build();

        Comment comment1 = Comment.builder()
                .id(1)
                .contents("test contents1")
                .member(member1)
                .post(post1)
                .build();

        Reply reply1 = Reply.builder()
                .id(1)
                .contents("대댓글 test contents1")
                .member(member1)
                .parentComment(comment1)
                .build();

        Reply reply2 = Reply.builder()
                .id(2)
                .contents("대댓글 test contents2")
                .member(member1)
                .parentComment(comment1)
                .build();

        List<Reply> replyList = new ArrayList<>();
        replyList.add(reply1);
        replyList.add(reply2);
//        comment1.setReplyList(replyList);


        memberRepository.save(member1);
        postRepository.save(post1);
        commentRepositroy.save(comment1);
        replyRepository.save(reply1);
        replyRepository.save(reply2);

        List<Comment> comments = commentRepositroy.findAll();

        List<FindCommentResponse> list = comments.stream().map(this::converToFindCommentResponse).collect(Collectors.toList());

        System.out.println("print list");
    }

    FindCommentResponse converToFindCommentResponse(Comment comment) {
        return FindCommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost().getId())
                .email(comment.getMember().getEmail())
                .nickname(comment.getMember().getNickname())
                .contents(comment.getContents())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .replyList(convertToReplyDTOs(comment.getReplyList()))
                .build();
    }

    List<ReplyDTO> convertToReplyDTOs(List<Reply> replyList) {
        return replyList.stream().map(reply -> ReplyDTO.builder()
                .reply_id(reply.getId())
                .email(reply.getMember().getEmail())
                .nickname(reply.getMember().getNickname())
                .contents(reply.getContents())
                .createdDate(reply.getCreatedDate())
                .updatedDate(reply.getUpdatedDate())
                .build()
        ).collect(Collectors.toList());
    }
}