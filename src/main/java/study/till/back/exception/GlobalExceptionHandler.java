package study.till.back.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.till.back.dto.CommonResponse;
import study.till.back.dto.exception.ErrorResponse;
import study.till.back.exception.comment.NotFoundCommentException;
import study.till.back.exception.common.NoDataException;
import study.till.back.exception.file.DeleteFileException;
import study.till.back.exception.member.*;
import study.till.back.exception.file.UploadFileException;
import study.till.back.exception.post.NotFoundPostException;
import study.till.back.exception.redis.NotEqualsRedisException;
import study.till.back.exception.redis.NotFoundRedisException;
import study.till.back.exception.reply.NotFoundReplyException;
import study.till.back.exception.token.ExpiredTokenException;
import study.till.back.exception.token.InvalidTokenException;
import study.till.back.exception.token.UnauthorizedTokenException;
import study.till.back.exception.token.UnsupportedTokenException;

import static study.till.back.dto.exception.ErrorCode.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<ErrorResponse> constraintViolationException() {
        log.error("ConstraintViolationException throw Exception : {}", DUPLICATED_KEY);
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    protected ResponseEntity<ErrorResponse> dataIntegrityViolationException() {
        log.error("DataIntegrityViolationException throw Exception : {}", DATA_INTEGRITY_VIOLATION);
        return ErrorResponse.toResponseEntity(DATA_INTEGRITY_VIOLATION);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
            builder.append(" [");
            builder.append(fieldError.getField());
            builder.append("]에 ");
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        String message = builder.toString();
        log.error("MethodArgumentNotValidException throw Exception : {}", message);

        CommonResponse commonResponse = CommonResponse.builder()
                .status(Integer.toString(status.value()))
                .message(message)
                .build();

        return ResponseEntity.status(status.value()).body(commonResponse);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = { NotFoundMemberException.class })
    protected ResponseEntity<ErrorResponse> notFoundMemberException(RuntimeException e) {
        log.error("RuntimeException throw notFoundMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler(value = { NotMatchMemberException.class })
    protected ResponseEntity<ErrorResponse> updateNotMatchMemberException(RuntimeException e) {
        log.error("RuntimeException throw updateNotMatchMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler(value = { NotFoundPostException.class })
    protected ResponseEntity<ErrorResponse> notFoundPostException(RuntimeException e) {
        log.error("RuntimeException throw notFoundPostException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler(value = { DuplicateMemberException.class })
    protected ResponseEntity<ErrorResponse> duplicateMemberException(RuntimeException e) {
        log.error("RuntimeException throw duplicateMemberException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(DUPLICATED_KEY);
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

    @ExceptionHandler(value = { NotFoundCommentException.class })
    protected ResponseEntity<ErrorResponse> notFoundCommentException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundCommentException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler(value = { NotFoundReplyException.class })
    protected ResponseEntity<ErrorResponse> notFoundReplyException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundReplyException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND);
    }

    @ExceptionHandler(value = { NoDataException.class })
    protected ResponseEntity<ErrorResponse> noDataException(RuntimeException e) {
        log.error("RuntimeException throw NoDataException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NO_DATA);
    }

    @ExceptionHandler(value = { UploadFileException.class })
    protected ResponseEntity<ErrorResponse> uploadFileException(RuntimeException e) {
        log.error("RuntimeException throw uploadFileException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(CANNOT_UPLOAD_FILE);
    }

    @ExceptionHandler(value = { DeleteFileException.class })
    protected ResponseEntity<ErrorResponse> deleteFileException(RuntimeException e) {
        log.error("RuntimeException throw deleteFileException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(CANNOT_DELETE_FILE);
    }

    @ExceptionHandler(value = { InvalidTokenException.class })
    protected ResponseEntity<ErrorResponse> invalidTokenException(RuntimeException e) {
        log.error("RuntimeException throw InvalidTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(INVALID_TOKEN);
    }
    @ExceptionHandler(value = { ExpiredTokenException.class })
    protected ResponseEntity<ErrorResponse> expiredTokenException(RuntimeException e) {
        log.error("RuntimeException throw ExpiredTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(EXPIRED_TOKEN);
    }

    @ExceptionHandler(value = { UnsupportedTokenException.class })
    protected ResponseEntity<ErrorResponse> unsupportedTokenException(RuntimeException e) {
        log.error("RuntimeException throw UnsupportedTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(UNSUPPORTED_TOKEN);
    }

    @ExceptionHandler(value = { UnauthorizedTokenException.class })
    protected ResponseEntity<ErrorResponse> unauthorizedTokenException(RuntimeException e) {
        log.error("RuntimeException throw UnauthorizedTokenException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(UNAUTHORIZED_TOKEN);
    }

    @ExceptionHandler(value = { NotFoundRedisException.class })
    protected ResponseEntity<ErrorResponse> notFoundRedisException(RuntimeException e) {
        log.error("RuntimeException throw NotFoundRedisException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_FOUND_REDIS_VALUE);
    }

    @ExceptionHandler(value = { NotEqualsRedisException.class })
    protected ResponseEntity<ErrorResponse> notEqualsRedisException(RuntimeException e) {
        log.error("RuntimeException throw NotEqualsRedisException : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(NOT_EQUALS_REDIS_VALUE);
    }
}
