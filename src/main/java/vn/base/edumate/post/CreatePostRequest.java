package vn.base.edumate.post;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequest {

    private Long id;

    @NotBlank(message = "TITLE_POST_NOT_VALID")
    @Size(max = 100, message = "TITLE_POST_OUT_OF_RANGE")
    private String title;

    @Size(max = 1000, message = "CONTENT_POST_OUT_OF_RANGE")
    private String content;

   @NotNull(message = "TAG_ID_REQUIRED")
    private Long tagId;

    private String userId;

    private List<Long> imageIds;
}
