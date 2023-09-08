package study.till.back.exception.member;

public class NotMatchMemberException extends RuntimeException {
    public NotMatchMemberException() {
        super("수정하실 컨텐츠의 회원과 등록된 회원이 일치하지 않습니다.");
    }
}