package vn.base.edumate.user.controller;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.CreateUserStatusHistory;
import vn.base.edumate.user.service.UserService;
import vn.base.edumate.user.service.UserStatusHistoryService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserStatusHistoryService userStatusHistoryService;
    UserService userService;

    @PostMapping("/{userId}/status-histories")
    public DataResponse<Integer> createUserStatusHistoryIfUserViolation(
            @PathVariable String userId, @RequestBody CreateUserStatusHistory createUserStatusHistory) {

        return DataResponse.<Integer>builder()
                .message("thêm trạng thái user thành công")
                .data(userStatusHistoryService.saveUserStatusHistoryIfUserViolation(userId, createUserStatusHistory))
                .build();
    }
    @GetMapping("/current-user")
    public DataResponse<UserResponse> getCurrentUser() {
        return DataResponse.<UserResponse>builder()
                .message("success")
                .data(userService.getCurrentUserToResponse())
                .build();
    }
}
