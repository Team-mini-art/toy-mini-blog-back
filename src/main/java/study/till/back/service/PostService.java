package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.FindPostResponse;
import study.till.back.dto.PostRequest;
import study.till.back.dto.PostResponse;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<FindPostResponse> findPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> FindPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .build()
        ).collect(Collectors.toList());
    }

    public ResponseEntity<PostResponse> createPost(PostRequest postRequest) {
        Member member = memberRepository.findById(postRequest.getMember_id()).orElse(null);

        PostResponse postResponse;
        if (member == null) {
            postResponse = PostResponse.builder()
                    .status("FAIL")
                    .message("등록된 회원 정보가 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(postResponse);
        }

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        postResponse = PostResponse.builder()
                .status("SUCCESS")
                .message("게시글 저장 성공")
                .build();
        return ResponseEntity.ok(postResponse);
    }

    public ResponseEntity<PostResponse> updatePost(PostRequest postRequest) {
        Post post = postRepository.findById(postRequest.getId()).orElse(null);

        PostResponse postResponse;
        if (post == null) {
            postResponse = PostResponse.builder()
                    .status("FAIL")
                    .message("존재하지 않는 게시글입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(postResponse);
        }

        post.setTitle(postRequest.getTitle());
        post.setContents(postRequest.getContents());
        post.setUpdateDate(LocalDateTime.now());
        postRepository.save(post);

        postResponse = PostResponse.builder()
                .status("SUCCESS")
                .message("게시글이 수정되었습니다.")
                .build();
        return ResponseEntity.ok(postResponse);
    }
}
