package study.till.back.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdDate;
}
