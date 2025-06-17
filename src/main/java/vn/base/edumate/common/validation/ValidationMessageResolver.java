package vn.base.edumate.common.validation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.base.edumate.common.exception.ErrorCode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ValidationMessageResolver {

    public String resolveMessage(String fieldName, Class<?> targetClass, String errorMessage) {

        ErrorCode errorCode = getErrorCode(errorMessage);

        if (errorCode == null) {
            return errorMessage;
        }

        try {
            Field field = targetClass.getDeclaredField(fieldName);
            Map<String, Object> constraints = extractConstraints(field);

            return errorCode.formatMessage(constraints.values().toArray());

        } catch (NoSuchFieldException ignored) {
            log.error("Không tìm thấy trường {} trong class {}", fieldName, targetClass.getName());
        }

        return errorMessage;
    }

    private ErrorCode getErrorCode(String errorMessage) {
        for (ErrorCode code : ErrorCode.values()) {
            if (code.name().equals(errorMessage)) {
                return code;
            }
        }
        return null;
    }

    private Map<String, Object> extractConstraints(Field field) {
        Map<String, Object> constraints = new HashMap<>();

        if (field.isAnnotationPresent(Size.class)) {
            Size size = field.getAnnotation(Size.class);
            constraints.put("min", size.min());
            constraints.put("max", size.max());
        }

        if (field.isAnnotationPresent(Max.class)) {
            Max max = field.getAnnotation(Max.class);
            constraints.put("max", max.value());
        }

        if (field.isAnnotationPresent(Min.class)) {
            Min min = field.getAnnotation(Min.class);
            constraints.put("min", min.value());
        }

        if (field.isAnnotationPresent(Pattern.class)) {
            Pattern pattern = field.getAnnotation(Pattern.class);
            constraints.put("regex", pattern.regexp());
        }

        return constraints;
    }
}
