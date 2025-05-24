package vn.base.edumate.user.dto.request;

import lombok.Builder;
import lombok.Data;
import vn.base.edumate.common.util.UserStatusCode;

@Data
@Builder
public class CreateUserStatusHistory {
    private UserStatusCode statusCode;
    private String reason;
}
