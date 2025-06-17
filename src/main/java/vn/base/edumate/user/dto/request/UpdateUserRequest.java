package vn.base.edumate.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.entity.UserStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private MultipartFile file;
    private UserStatusCode status;
}