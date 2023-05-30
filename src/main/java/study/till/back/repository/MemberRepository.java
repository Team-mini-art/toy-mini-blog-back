package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.dto.MemberDto;
import study.till.back.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
