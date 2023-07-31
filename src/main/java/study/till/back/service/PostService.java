package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.FindPostResponse;
import study.till.back.dto.PostRequest;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.UpdatePostRequest;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.exception.NotFoundMemberException;
import study.till.back.exception.NotFoundPostException;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public ResponseEntity<List<FindPostResponse>> findPosts() {
        List<Post> posts = postRepository.findAll();

        List<FindPostResponse> findPostResponses = posts.stream().map(post -> FindPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(findPostResponses);
    }

    public ResponseEntity<FindPostResponse> findPost(Long postId) {
        Post post = postRepository.findById(postId).get();

        FindPostResponse findPostResponse = FindPostResponse.builder()
                .id(post.getId())
                .email(post.getMember().getEmail())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .build();

        return ResponseEntity.ok(findPostResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> createPost(PostRequest postRequest) {
        Member member = memberRepository.findById(postRequest.getEmail()).orElse(null);
        if (member == null) throw new NotFoundMemberException();

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .member(member)
                .createdDate(LocalDateTime.now())
                .build();
        postRepository.save(post);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("게시글 저장 성공")
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> updatePost(UpdatePostRequest postRequest) {
        Member member = memberRepository.findById(postRequest.getEmail()).orElse(null);
        if (member == null) throw new NotFoundMemberException();

        Post post = postRepository.findById(postRequest.getId()).orElse(null);
        if (post == null) throw new NotFoundPostException();


        post.setTitle(postRequest.getTitle());
        post.setContents(postRequest.getContents());
        post.setUpdateDate(LocalDateTime.now());
        postRepository.save(post);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("게시글이 수정되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
    @Transactional
    public ResponseEntity<CommonResponse> deletePost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) throw new NotFoundPostException();

        postRepository.deleteById(id);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("게시글이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
