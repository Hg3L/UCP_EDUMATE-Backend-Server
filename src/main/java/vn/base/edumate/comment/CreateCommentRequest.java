package vn.base.edumate.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateCommentRequest {
    @NotBlank(message = "COMMENT_NOT_VALID")
    String content;
    Long imageId;
}
