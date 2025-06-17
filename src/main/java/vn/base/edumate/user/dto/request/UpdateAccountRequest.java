package vn.base.edumate.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountRequest {

    @NotBlank
    String id;

    @NotBlank
    String status;

    @Nullable
    String reason;
}
