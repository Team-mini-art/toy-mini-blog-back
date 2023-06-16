package study.till.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.till.back.entity.Post;
import study.till.back.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }
}
