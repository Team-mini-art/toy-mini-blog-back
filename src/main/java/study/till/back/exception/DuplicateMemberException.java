package study.till.back.exception;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super("중복된 email입니다.");
    }
}