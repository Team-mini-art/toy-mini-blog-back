package study.till.back.dto.member;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class MemberRequest {
    @NotNull(message = "email cannot be null")
    String email;

    @NotNull(message = "nickname cannot be null")
    String nickname;

    MultipartFile multipartFile;
}
