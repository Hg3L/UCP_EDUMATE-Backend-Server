package vn.base.edumate.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.CreateUserStatusHistory;
import vn.base.edumate.user.dto.request.UpdateUserRequest;
import vn.base.edumate.user.service.UserService;
import vn.base.edumate.user.service.UserStatusHistoryService;

import java.io.IOException;

@Slf4j
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
    @PutMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public DataResponse<UserResponse> updateUser( @RequestPart("username") String username,
                                                  @RequestPart(value = "multipartFiles", required = false) MultipartFile multipartFiles) throws IOException {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username(username)
                .file(multipartFiles)
                .build();
        UserResponse userResponse = userService.updateUser(updateUserRequest);
        log.info("username",userResponse.getUsername());
        return DataResponse.<UserResponse>builder()
                .message("Cập nhật thông tin người dùng thành công")
                .data(userResponse)
                .build();
    }
}
