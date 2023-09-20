package study.till.back.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import study.till.back.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
public class FindMemberResponse {
    private String email;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static FindMemberResponse from(Member member) {
        return builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .createdDate(member.getCreatedDate())
                .updatedDate(member.getUpdatedDate())
                .build();
    }
}
