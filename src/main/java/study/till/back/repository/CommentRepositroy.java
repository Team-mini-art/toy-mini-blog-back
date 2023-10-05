package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.Comment;

import java.util.List;

public interface CommentRepositroy extends JpaRepository<Comment, Long> {
}
