package study.till.back.exception.reply;

public class NotFoundReplyException extends RuntimeException {
    public NotFoundReplyException() {
        super("DB에 저장된 reply 정보를 찾을 수 없습니다.");
    }
}
