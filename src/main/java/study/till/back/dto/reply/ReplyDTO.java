package study.till.back.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.till.back.entity.Reply;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyDTO {
    private long reply_id;
    private String email;
    private String nickname;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ReplyDTO fromEntity(Reply reply) {
        return ReplyDTO.builder()
                .reply_id(reply.getId())
                .email(reply.getMember().getEmail())
                .nickname(reply.getMember().getNickname())
                .contents(reply.getContents())
                .createdDate(reply.getCreatedDate())
                .updatedDate(reply.getUpdatedDate())
                .build();
    }

    public static List<ReplyDTO> fromEntities(List<Reply> replies) {
        return replies.stream()
                .map(ReplyDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
