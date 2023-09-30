package study.till.back.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindCommentResponse {
    private Long id;
    private Long post_id;
    private Long parent_comment_id;
    private String email;
    private String nickname;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
