package study.till.back.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.till.back.dto.exception.ErrorResponse;

import static study.till.back.dto.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class })
    protected ResponseEntity<ErrorResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", DUPLICATED_KEY);
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = { NotFoundMemberException.class })
    protected ResponseEntity<ErrorResponse> notFoundMemberException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = { NotFoundPostException.class })
    protected ResponseEntity<ErrorResponse> notFoundPostException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundPostException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = { DuplicateMemberException.class })
    protected ResponseEntity<ErrorResponse> duplicateMemberException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundPostException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY, e.getMessage());
    }

    @ExceptionHandler(value = { InvalidPasswordException.class })
    protected ResponseEntity<ErrorResponse> invalidPasswordException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundPostException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(INVALID_PASSWORD, e.getMessage());
    }
}
