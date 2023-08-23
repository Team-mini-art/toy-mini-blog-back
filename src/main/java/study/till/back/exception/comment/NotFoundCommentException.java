package study.till.back.exception.comment;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
        super("DB에 저장된 comment 정보를 찾을 수 없습니다.");
    }
}
