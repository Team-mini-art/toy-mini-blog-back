package study.till.back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.post.FindPostPageResponse;
import study.till.back.dto.post.FindPostResponse;
import study.till.back.dto.post.PostRequest;
import study.till.back.dto.CommonResponse;
import study.till.back.entity.Member;
import study.till.back.entity.Post;
import study.till.back.exception.common.NoDataException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.exception.member.NotMatchMemberException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.repository.MemberRepository;
import study.till.back.repository.PostRepository;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public ResponseEntity<FindPostPageResponse> findPosts(Pageable pageable, String email, String title, String contents) {
        Specification<Post> spec = Specification.where(Specification.<Post>where(null));

        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, builder) -> {
                Join<Post, Member> memberPostJoin = root.join("member");
                return builder.like(memberPostJoin.get("email"), "%" + email + "%");
            });
        }

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("title"), "%" + title + "%"));
        }

        if (contents != null && !contents.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("contents"), "%" + contents + "%"));
        }

        Page<Post> all = postRepository.findAll(spec, pageable);

        if (all.isEmpty()) {
            throw new NoDataException();
        }

        List<FindPostResponse> posts = all.stream().map(FindPostResponse::from).collect(Collectors.toList());
        FindPostPageResponse findPostPageResponse = FindPostPageResponse.from(all, posts);
        return ResponseEntity.ok(findPostPageResponse);
    }

    public ResponseEntity<FindPostResponse> findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);
        FindPostResponse findPostResponse = FindPostResponse.from(post);
        return ResponseEntity.ok(findPostResponse);
    }

    @Transactional
    public ResponseEntity<CreateCommonResponse> createPost(PostRequest postRequest) {
        Member member = memberRepository.findById(postRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .member(member)
                .build();
        postRepository.save(post);

        long id = post.getId();

        CreateCommonResponse createCommonResponse = CreateCommonResponse.builder()
                .id(id)
                .status("SUCCESS")
                .message("게시글 작성이 완료되었습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommonResponse);
    }

    @Transactional
    public ResponseEntity<CommonResponse> updatePost(Long id, PostRequest postRequest) {
        memberRepository.findById(postRequest.getEmail()).orElseThrow(NotFoundMemberException::new);

        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);

        if (!postRequest.getEmail().equals(post.getMember().getEmail())) throw new NotMatchMemberException();

        post.updatePost(postRequest.getTitle(), postRequest.getContents());
        postRepository.save(post);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("게시글이 수정되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
    @Transactional
    public ResponseEntity<CommonResponse> deletePost(Long id) {
        postRepository.findById(id).orElseThrow(NotFoundPostException::new);

        postRepository.deleteById(id);

        CommonResponse commonResponse = CommonResponse.builder()
                .status("SUCCESS")
                .message("게시글이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
