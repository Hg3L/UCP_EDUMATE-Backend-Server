package vn.base.edumate.post;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequest {
    private Long id;
    @NotBlank(message = "TITLE_POST_NOT_VALID")
    @Max(value = 100, message = "TITLE_POST_OUT_OF_RANGE")
    private String title;
    @Max(value = 1000,message = "CONTENT_POST_OUT_OF_RANGE")
    private String content;
    private Long tagId;
    private String userId;
    List<Long> imageIds;
}
