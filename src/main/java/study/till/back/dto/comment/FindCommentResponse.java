package study.till.back.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindCommentResponse {
    private Long id;
    private Long post_id;
    private String email;
    private String contents;
}
