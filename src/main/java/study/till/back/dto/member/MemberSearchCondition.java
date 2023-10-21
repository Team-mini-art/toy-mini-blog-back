package study.till.back.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberSearchCondition {
    private String email;
    private String nickname;
}
