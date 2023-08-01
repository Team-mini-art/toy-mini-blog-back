package study.till.back.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("비밀번호는 대문자, 숫자, 특수문자 포함 10자리 이상이어야 합니다.");
    }
}