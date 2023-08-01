package study.till.back.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    HANDLER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 핸들러를 찾을 수 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버문제가 발생했습니다. 관리자에게 문의해주세요."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    DUPLICATED_KEY(HttpStatus.CONFLICT, "중복된 key를 사용할 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호는 대문자, 숫자, 특수문자 포함 10자리 이상이어야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
