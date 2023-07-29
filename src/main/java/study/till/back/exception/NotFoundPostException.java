package study.till.back.exception;

public class NotFoundPostException extends RuntimeException {
    public NotFoundPostException() {
        super("DB에 저장된 post 정보를 찾을 수 없습니다.");
    }
}
