package vn.base.edumate.common.exception;

public class InvalidTokenTypeException extends BaseApplicationException {

    public InvalidTokenTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
