package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public ResponseEntity<FindPostPageResponse> findPosts(Pageable pageable) {
        Page<Post> contents = postRepository.findAll(pageable);

        if (contents.isEmpty()) {
            throw new NoDataException();
        }

        List<FindPostResponse> posts = contents.stream().map(FindPostResponse::from).collect(Collectors.toList());

        FindPostPageResponse findPostPageResponse = FindPostPageResponse.builder()
                .posts(posts)
                .totalElements(contents.getTotalElements())
                .totalPages(contents.getTotalPages())
                .pageNumber(contents.getNumber())
                .pageSize(contents.getSize())
                .hasPrevious(contents.hasPrevious())
                .hasNext(contents.hasNext())
                .build();

        return ResponseEntity.ok(findPostPageResponse);
    }

    public ResponseEntity<FindPostResponse> findPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NotFoundPostException::new);

        FindPostResponse findPostResponse = FindPostResponse.builder()
                .id(post.getId())
                .email(post.getMember().getEmail())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .updatedDate(post.getUpdatedDate())
                .build();

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
