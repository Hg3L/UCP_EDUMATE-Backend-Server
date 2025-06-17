package vn.base.edumate.authentication.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ForgotPasswordRequest {

    @Email(message = "INVALID_EMAIL")
    @NotBlank
    private String email;

}
