package study.till.back.dto.post;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "title cannot be null")
    private String title;

    @NotNull(message = "contents cannot be null")
    private String contents;
}
