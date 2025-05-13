package vn.base.edumate.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreatePostRequest {
    private String title;
    private String content;
    private Long tagId;
    private String userId;
    List<Long> imageIds;
}
