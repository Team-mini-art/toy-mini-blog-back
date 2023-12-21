package study.till.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    private String status;
    private String message;

    public static CommonResponse createCommonResponse(String status, String message) {
        return CommonResponse.builder()
                .status(status)
                .message(message)
                .build();
    }
}
