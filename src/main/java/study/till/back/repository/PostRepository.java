package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
}
