package study.till.back.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdDate;
}
