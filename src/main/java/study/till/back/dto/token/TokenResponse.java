package study.till.back.dto.token;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TokenResponse {
    private String newAccessToken;
}
