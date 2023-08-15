package study.till.back.exception.token;

public class ExpiredAccessTokenException extends RuntimeException {
    public ExpiredAccessTokenException() {
        super("access_token_expired");
    }
}
