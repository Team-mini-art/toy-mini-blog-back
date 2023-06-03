package study.till.back.dto;

import lombok.*;
import study.till.back.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdDate;

    public Member toEntity() {
        Member member = new Member();
        member.setEmail(this.email);
        member.setPassword(this.password);
        member.setNickname(this.nickname);
        member.setCreatedDate(LocalDateTime.now());
        return member;
    }
}
