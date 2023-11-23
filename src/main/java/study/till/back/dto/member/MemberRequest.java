package study.till.back.dto.member;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MemberRequest {
    @NotEmpty(message = "이메일을 입력해주세요.")
    String email;
    String nickname;
    MultipartFile multipartFile;
}
