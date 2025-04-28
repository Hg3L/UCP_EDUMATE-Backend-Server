package vn.base.edumate.common.exception;

import lombok.Getter;

@Getter
public class BaseApplicationException extends RuntimeException {

    public BaseApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private final ErrorCode errorCode;
}
