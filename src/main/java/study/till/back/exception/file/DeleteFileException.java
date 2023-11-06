package study.till.back.exception.file;

public class DeleteFileException extends RuntimeException {
    public DeleteFileException() {
        super("파일 삭제에 실패하였습니다.");
    }
}