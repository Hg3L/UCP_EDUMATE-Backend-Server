package vn.base.edumate.post;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequest {
    private Long id;
    private String title;
    private String content;
    private Long tagId;
    private String userId;
    List<Long> imageIds;
}
