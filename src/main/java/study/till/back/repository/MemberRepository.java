package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password);
    Member findByEmail(String email);
}
