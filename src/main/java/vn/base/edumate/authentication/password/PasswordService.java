package vn.base.edumate.authentication.password;

import jakarta.servlet.http.HttpServletRequest;

public interface PasswordService {

    String sendLinkResetPassword(ForgotPasswordRequest request);

    void handleResetToken(HttpServletRequest request);

    void resetPassword(ResetPasswordRequest request, HttpServletRequest httpServletRequest);
}
