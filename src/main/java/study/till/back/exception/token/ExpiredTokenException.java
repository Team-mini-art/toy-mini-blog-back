package study.till.back.exception.token;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException() {
        super("만료된 토큰입니다.");
    }
}
