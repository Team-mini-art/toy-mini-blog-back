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
    DUPLICATED_KEY(HttpStatus.CONFLICT, "중복된 key를 사용할 수 없습니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "데이터베이스의 무결성 제약 조건을 위반하였습니다."),
    INVALID_EMAIL(HttpStatus.FORBIDDEN, "이메일 형식이 올바르지 않습니다."),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "비밀번호는 대문자, 숫자, 특수문자 포함 10자리 이상이어야 합니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "access_token_expired"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "refresh_token_expired"),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "인증되지 않은 토큰입니다."),
    NOT_MATCH(HttpStatus.FORBIDDEN, "수정하실 컨텐츠의 회원과 등록된 회원이 일치하지 않습니다."),
    NO_DATA(HttpStatus.NOT_FOUND, "No Data"),
    CANNOT_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
