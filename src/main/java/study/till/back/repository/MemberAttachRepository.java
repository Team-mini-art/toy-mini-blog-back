package study.till.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.till.back.entity.MemberAttach;

import java.util.List;

public interface MemberAttachRepository extends JpaRepository<MemberAttach, Long> {
    List<MemberAttach> findByMember_EmailAndContentTypeContaining(String email, String contentType);

    void deleteByMember_Email(String email);
}
