package study.till.back.dto.member;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class MemberRequest {
    String email;
    String nickname;
    MultipartFile multipartFile;
}
