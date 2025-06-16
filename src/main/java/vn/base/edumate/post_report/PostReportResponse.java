package vn.base.edumate.post_report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;
import vn.base.edumate.post.PostResponse;
import vn.base.edumate.user.dto.UserResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PostReportResponse extends AbstractDTO {
    private String reason;
    private PostResponse post;
    private UserResponse user;
}
