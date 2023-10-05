package study.till.back.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.till.back.dto.reply.ReplyDTO;
import study.till.back.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindCommentResponse {
    private Long id;
    private Long post_id;
    private String email;
    private String nickname;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<ReplyDTO> replyList = new ArrayList<>();

    public static FindCommentResponse fromEntity(Comment comment) {
        return FindCommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost().getId())
                .email(comment.getMember().getEmail())
                .nickname(comment.getMember().getNickname())
                .contents(comment.getContents())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .replyList(ReplyDTO.fromEntities(comment.getReplyList()))
                .build();
    }
}
