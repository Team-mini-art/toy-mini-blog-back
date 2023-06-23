package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import study.till.back.dto.CreatePostRequest;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public HttpStatus createPost(CreatePostRequest createPostRequest) {
        Member member = memberRepository.findById(createPostRequest.getMember_id()).orElse(null);

        if (member == null) {
            return HttpStatus.BAD_REQUEST;
        }

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .contents(createPostRequest.getContents())
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
        return HttpStatus.CREATED;
    }
}
