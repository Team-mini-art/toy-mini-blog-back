package study.till.back.exception.file;

public class UploadFileException extends RuntimeException {
    public UploadFileException() {
        super("파일 업로드에 실패하였습니다.");
    }
}