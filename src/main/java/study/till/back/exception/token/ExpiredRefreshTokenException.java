package study.till.back.exception.token;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException() {
        super("refresh_token_expired");
    }
}
