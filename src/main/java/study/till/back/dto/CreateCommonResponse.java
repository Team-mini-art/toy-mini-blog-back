package study.till.back.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommonResponse {
    private Long id;
    private String status;
    private String message;
}
