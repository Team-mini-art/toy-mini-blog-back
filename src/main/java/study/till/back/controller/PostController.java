package study.till.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import study.till.back.dto.CreatePostRequest;
import study.till.back.entity.Post;
import study.till.back.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<Post> findPosts() {
        return postService.findPosts();
    }

    @PostMapping("/posts")
    public HttpStatus createPost(@RequestBody CreatePostRequest createPostRequest) {
        return postService.createPost(createPostRequest);
    }
}
