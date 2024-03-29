package study.till.back.dto.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String status;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status("FAIL")
                        .message(errorCode.getMessage())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status("FAIL")
                        .message(message)
                        .build()
                );
    }
}
