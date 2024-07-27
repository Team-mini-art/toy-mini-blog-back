package study.till.back.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.till.back.entity.Comment;
import study.till.back.entity.Reply;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c FROM Comment c LEFT JOIN FETCH c.replyList",
            countQuery = "SELECT COUNT(c) FROM Comment c")
    Page<Comment> findAllWithRepliesList(Pageable pageable);

    @Query("SELECT c FROM Comment c")
    Page<Comment> findAllComments(Pageable pageable);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replyList WHERE c IN :comments")
    List<Comment> findCommentsWithReplies(@Param("comments") List<Comment> comments);
}