package study.till.back.dto.token;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@Getter
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}