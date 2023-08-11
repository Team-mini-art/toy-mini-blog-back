package study.till.back.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
