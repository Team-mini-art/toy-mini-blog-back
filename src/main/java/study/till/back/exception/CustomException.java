package study.till.back.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import study.till.back.dto.exception.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
