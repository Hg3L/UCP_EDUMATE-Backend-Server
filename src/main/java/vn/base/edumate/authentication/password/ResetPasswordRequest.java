package vn.base.edumate.authentication.password;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import vn.base.edumate.common.validation.annotation.PasswordMatch;

@Getter
@PasswordMatch(message = "INVALID_PASSWORD_CONFIRM")
public class ResetPasswordRequest {

    @Size(min = 6, max = 18, message = "INVALID_PASSWORD")
    private String password;

    private String confirmPassword;
}
