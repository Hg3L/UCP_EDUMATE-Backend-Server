package vn.base.edumate.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;
import vn.base.edumate.image.ImageResponse;
import vn.base.edumate.post.PostResponse;
import vn.base.edumate.user.dto.UserResponse;


@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class CommentResponse extends AbstractDTO {
    String content;
    int likes;
    ImageResponse image;
    UserResponse user;
    PostResponse post;
    CommentResponse parent;
    int repliesCount;
    boolean liked;
}
