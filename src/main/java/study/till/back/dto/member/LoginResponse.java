package study.till.back.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import study.till.back.dto.token.TokenInfo;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String status;
    private String message;
    private String email;
    private String nickname;
    private TokenInfo tokenInfo;
}
