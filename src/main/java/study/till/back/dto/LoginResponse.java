package study.till.back.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
public class LoginResponse {
    private String status;
    private String message;
    private Long id;
    private String email;
    private String nickname;
    private TokenInfo tokenInfo;
}
