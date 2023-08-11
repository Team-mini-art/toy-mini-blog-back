package study.till.back.dto.member;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
public class FindMemberResponse {
    private String email;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
