package vn.base.edumate.common.exception;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.common.base.ErrorResponse;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @Value("${system.exception.show.client-info}")
    private boolean showClientInfo;

    /**
     *  Handle runtime exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlingRuntimeException(Exception e, WebRequest request) {
        log.error("Runtime exception: ", e);

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .status(ErrorCode.UNCATEGORIZED.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .build();
    }

    /**
     * Handle base application exception
     */
    @ExceptionHandler(BaseApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlingApplicationException(BaseApplicationException e, WebRequest request) {
        log.error("Application exception: ", e);
        ErrorCode errorCode = e.getErrorCode();
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    /**
     * Handle resource not found exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlingResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.error("Resource not found: ", e);
        ErrorCode errorCode = e.getErrorCode();
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    /**
     * Handle firebase authentication exception
     */
    @ExceptionHandler(CustomFirebaseAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlingCustomFirebaseAuthException(CustomFirebaseAuthException e, WebRequest request) {
        log.error("Firebase exception: ", e);
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(e.getMessage())
                .build();
    }

    /**
     * Handle resource not found exception
     */
    @ExceptionHandler(InvalidTokenTypeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlingInvalidTokenTypeException(InvalidTokenTypeException e, WebRequest request) {
        log.error("Invalid token: ", e);
        ErrorCode errorCode = e.getErrorCode();
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlingMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        log.error("Resource not found: ", e.getCause().getMessage());
        Throwable root = e;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        ErrorCode errorCode = ErrorCode.valueOf(root.getMessage());
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlingExpiredJwtException(ExpiredJwtException e, WebRequest request) {
        log.error("Invalid token: ", e);
        ErrorCode errorCode = ErrorCode.EXPIRED_TOKEN;
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .path(request.getDescription(showClientInfo).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }
}
