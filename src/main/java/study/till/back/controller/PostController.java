package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.FindPostResponse;
import study.till.back.dto.PostRequest;
import study.till.back.dto.PostResponse;
import study.till.back.entity.Post;
import study.till.back.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<FindPostResponse> findPosts() {
        return postService.findPosts();
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    @PutMapping("/posts")
    public ResponseEntity<PostResponse> updatePost(@RequestBody PostRequest postRequest) {
        return postService.updatePost(postRequest);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable("id") Long id) {
        return postService.deletePost(id);
    }
}
