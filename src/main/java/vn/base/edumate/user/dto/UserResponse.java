package vn.base.edumate.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;

import java.util.Date;


@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse  {
    String id;
    String createdBy;
    String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date updatedAt;
    String username;
    String avatarUrl;
}
