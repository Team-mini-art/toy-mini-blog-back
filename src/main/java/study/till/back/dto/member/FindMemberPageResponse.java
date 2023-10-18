package study.till.back.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import study.till.back.entity.Member;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class FindMemberPageResponse {
    public List<FindMemberResponse> members;
    private long totalElements;
    private long totalPages;
    private int pageNumber;
    private int pageSize;
    private boolean hasPrevious;
    private boolean hasNext;

    public static FindMemberPageResponse from(Page<Member> contents, List<FindMemberResponse> members) {
        return builder()
                .members(members)
                .totalElements(contents.getTotalElements())
                .totalPages(contents.getTotalPages())
                .pageNumber(contents.getNumber())
                .pageSize(contents.getSize())
                .hasPrevious(contents.hasPrevious())
                .hasNext(contents.hasNext())
                .build();
    }
}
