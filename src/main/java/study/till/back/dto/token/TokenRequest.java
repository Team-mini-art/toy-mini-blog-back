package study.till.back.dto.token;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TokenRequest {
    private String accessToken;
    private String refreshToken;
}
