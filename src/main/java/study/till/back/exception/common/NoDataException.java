package study.till.back.exception.common;

public class NoDataException extends RuntimeException {
    public NoDataException() {
        super("No Data");
    }
}
