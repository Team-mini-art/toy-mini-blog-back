package study.till.back.exception.memberAttach;

public class UploadMemberAttachException extends RuntimeException {
    public UploadMemberAttachException() {
        super("파일 업로드에 실패하였습니다.");
    }
}