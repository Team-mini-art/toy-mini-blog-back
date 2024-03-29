package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CreateCommonResponse;
import study.till.back.dto.post.FindPostPageResponse;
import study.till.back.dto.post.FindPostResponse;
import study.till.back.dto.post.PostRequest;
import study.till.back.dto.CommonResponse;
import study.till.back.service.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<FindPostPageResponse> findPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String contents
    ) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return postService.findPosts(pageable, email, title, contents);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<FindPostResponse> findPost(@PathVariable("id") Long id) {
        return postService.findPost(id);
    }

    @PostMapping("/posts")
    public ResponseEntity<CreateCommonResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("id") Long id, @Valid @RequestBody PostRequest postRequest) {
        return postService.updatePost(id, postRequest);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("id") Long id) {
        return postService.deletePost(id);
    }
}


