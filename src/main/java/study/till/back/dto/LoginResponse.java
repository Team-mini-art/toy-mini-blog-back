package study.till.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String status;
    private String message;
    private Long id;
    private String email;
    private String nickname;
}
