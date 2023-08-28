package study.till.back.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.till.back.dto.exception.ErrorResponse;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.member.DuplicateMemberException;
import study.till.back.exception.member.InvalidEmailException;
import study.till.back.exception.member.InvalidPasswordException;
import study.till.back.exception.member.NotFoundMemberException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.exception.token.ExpiredAccessTokenException;
import study.till.back.exception.token.ExpiredRefreshTokenException;
import study.till.back.exception.token.UnauthorizedTokenException;

import static study.till.back.dto.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<ErrorResponse> ConstraintViolationException() {
        log.error("ConstraintViolationException throw Exception : {}", DUPLICATED_KEY);
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    protected ResponseEntity<ErrorResponse> DataIntegrityViolationException() {
        log.error("DataIntegrityViolationException throw Exception : {}", DATA_INTEGRITY_VIOLATION);
        return ErrorResponse.toResponseEntity(DATA_INTEGRITY_VIOLATION);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = { NotFoundMemberException.class })
    protected ResponseEntity<ErrorResponse> notFoundMemberException(RuntimeException e) {
        log.error("RuntimeException throw notFoundMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = { NotFoundPostException.class })
    protected ResponseEntity<ErrorResponse> notFoundPostException(RuntimeException e) {
        log.error("RuntimeException throw notFoundPostException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = { DuplicateMemberException.class })
    protected ResponseEntity<ErrorResponse> duplicateMemberException(RuntimeException e) {
        log.error("RuntimeException throw duplicateMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY, e.getMessage());
    }

    @ExceptionHandler(value = { InvalidEmailException.class })
    protected ResponseEntity<ErrorResponse> InvalidEmailException(RuntimeException e) {
        log.error("RuntimeException throw invalidEmailException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(INVALID_EMAIL);
    }

    @ExceptionHandler(value = { InvalidPasswordException.class })
    protected ResponseEntity<ErrorResponse> invalidPasswordException(RuntimeException e) {
        log.error("RuntimeException throw invalidPasswordException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(INVALID_PASSWORD);
    }

    @ExceptionHandler(value = { ExpiredAccessTokenException.class })
    protected ResponseEntity<ErrorResponse> expiredAccessTokenException(RuntimeException e) {
        log.error("RuntimeException throw expiredAccessTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(EXPIRED_ACCESS_TOKEN, e.getMessage());
    }

    @ExceptionHandler(value = { ExpiredRefreshTokenException.class })
    protected ResponseEntity<ErrorResponse> expiredRefreshTokenException(RuntimeException e) {
        log.error("RuntimeException throw expiredRefreshTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(EXPIRED_REFRESH_TOKEN, e.getMessage());
    }

    @ExceptionHandler(value = { UnauthorizedTokenException.class })
    protected ResponseEntity<ErrorResponse> invalidTokenException(RuntimeException e) {
        log.error("RuntimeException throw InvalidTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(UNAUTHORIZED_TOKEN);
    }

    @ExceptionHandler(value = {NotFoundCommentException.class })
    protected ResponseEntity<ErrorResponse> notFoundCommentException(RuntimeException e) {
        log.error("RuntimeException throw notFoundCommentException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND, e.getMessage());
    }
}
