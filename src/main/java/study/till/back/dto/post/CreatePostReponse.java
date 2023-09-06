package study.till.back.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostReponse {
    private Long id;
    private String status;
    private String message;
}
