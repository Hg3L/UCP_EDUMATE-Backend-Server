package vn.base.edumate.common.exception;

public class AccountLockedException extends BaseApplicationException {
    public AccountLockedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
