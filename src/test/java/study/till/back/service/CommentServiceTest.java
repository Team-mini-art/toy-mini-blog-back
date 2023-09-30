package study.till.back.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.till.back.entity.Comment;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.repository.CommentRepositroy;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepositroy commentRepositroy;

    @Test
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

        Comment comment2 = Comment.builder()
                .id(2)
                .contents("대댓글 test contents2")
                .member(member1)
                .post(post1)
                .parentComment(comment1)
                .build();

        memberRepository.save(member1);
        postRepository.save(post1);

        commentRepositroy.save(comment1);
        commentRepositroy.save(comment2);

        List<Comment> list = commentRepositroy.findAll();
        System.out.println("print list");
    }
}