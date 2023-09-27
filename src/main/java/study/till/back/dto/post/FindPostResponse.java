package study.till.back.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.till.back.entity.Post;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindPostResponse {
    private Long id;
    private String email;
    private String title;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static FindPostResponse from(Post post) {
        return builder()
                .id(post.getId())
                .email(post.getMember().getEmail())
                .title(post.getTitle())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .updatedDate(post.getUpdatedDate())
                .build();
    }
}
