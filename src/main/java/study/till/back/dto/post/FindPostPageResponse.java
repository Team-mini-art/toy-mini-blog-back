package study.till.back.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import study.till.back.entity.Post;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class FindPostPageResponse {
    public List<FindPostResponse> posts;
    private long totalElements;
    private long totalPages;
    private int pageNumber;
    private int pageSize;
    private boolean hasPrevious;
    private boolean hasNext;

    public static FindPostPageResponse from(Page<Post> contents, List<FindPostResponse> posts) {
        return builder()
                .posts(posts)
                .totalElements(contents.getTotalElements())
                .totalPages(contents.getTotalPages())
                .pageNumber(contents.getNumber())
                .pageSize(contents.getSize())
                .hasPrevious(contents.hasPrevious())
                .hasNext(contents.hasNext())
                .build();
    }
}
