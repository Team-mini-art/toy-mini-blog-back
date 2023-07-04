package study.till.back.dto;

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
    private String title;
    private String contents;
    private LocalDateTime createdDate;
}
