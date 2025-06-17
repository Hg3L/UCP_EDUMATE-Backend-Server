package vn.base.edumate.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.base.edumate.authentication.password.ResetPasswordRequest;
import vn.base.edumate.common.validation.annotation.PasswordMatch;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, ResetPasswordRequest> {

    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.message = constraintAnnotation.message();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ResetPasswordRequest passwordRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (passwordRequest.getPassword() == null
                || passwordRequest.getConfirmPassword() == null
                || !passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
