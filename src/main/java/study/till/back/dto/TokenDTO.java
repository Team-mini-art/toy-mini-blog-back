package study.till.back.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Builder
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
