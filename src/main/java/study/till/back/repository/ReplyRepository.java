package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
