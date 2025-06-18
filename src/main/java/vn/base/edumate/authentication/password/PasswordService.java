package vn.base.edumate.authentication.password;

import jakarta.servlet.http.HttpServletRequest;
import vn.base.edumate.token.TokenResponse;

public interface PasswordService {

    TokenResponse sendLinkResetPassword(ForgotPasswordRequest request);

    void handleResetToken(HttpServletRequest request);

    void resetPassword(ResetPasswordRequest request, HttpServletRequest httpServletRequest);
}
