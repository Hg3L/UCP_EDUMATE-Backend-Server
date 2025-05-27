package vn.base.edumate.user.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.util.UserStatusCode;

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
    String avatarUrl;
    UserStatusCode status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy HH:mm:ss")
    private LocalDateTime expiredAt;


}
