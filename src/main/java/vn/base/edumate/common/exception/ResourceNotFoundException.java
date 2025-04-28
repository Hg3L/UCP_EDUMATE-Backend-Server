package vn.base.edumate.common.exception;

public class ResourceNotFoundException extends BaseApplicationException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
