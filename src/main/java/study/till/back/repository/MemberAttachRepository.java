package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.MemberAttach;

import java.util.List;

public interface MemberAttachRepository extends JpaRepository<MemberAttach, Long> {
    List<MemberAttach> findByMember_Email(String email);
}
