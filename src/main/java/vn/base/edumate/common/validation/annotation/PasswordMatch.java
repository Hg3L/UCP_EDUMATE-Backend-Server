package vn.base.edumate.common.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import vn.base.edumate.common.validation.validator.PasswordMatchValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "INVALID_PASSWORD_CONFIRM";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
