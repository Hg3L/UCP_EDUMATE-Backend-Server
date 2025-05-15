package vn.base.edumate.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;
import vn.base.edumate.image.ImageResponse;
import vn.base.edumate.tag.TagResponse;
import vn.base.edumate.user.dto.UserResponse;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse extends AbstractDTO {
    private String content;
    private String title;
    private UserResponse author;
    private TagResponse tag;
    private Integer likeCount;
    private Integer commentCount;
    private List<ImageResponse> images;
}
