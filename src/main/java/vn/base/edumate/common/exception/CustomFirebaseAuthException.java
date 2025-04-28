package vn.base.edumate.common.exception;

import com.google.firebase.auth.AuthErrorCode;
import lombok.Getter;

@Getter
public class CustomFirebaseAuthException extends RuntimeException {

    private final int status;
    private final AuthErrorCode authErrorCode;

    public CustomFirebaseAuthException(int status, AuthErrorCode authErrorCode, String message) {
        super(message);
        this.status = status;
        this.authErrorCode = authErrorCode;
    }
}
