package study.till.back.dto.token;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TokenRequest {
    @NotEmpty(message = "email을 입력해주세요.")
    private String email;
    @NotEmpty(message = "refreshToken을 입력해주세요.")
    private String refreshToken;
}
