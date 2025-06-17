package vn.base.edumate.user.dto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.comment.CommentResponse;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.post.PostResponse;


@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String createdBy;
    String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date updatedAt;

    String username;
    String email;

    String avatarUrl;
    UserStatusCode status;

    String introduction;
    AuthMethod authMethod;

    int commentCount;
    int postCount;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy HH:mm:ss")
    private LocalDateTime expiredAt;




}
