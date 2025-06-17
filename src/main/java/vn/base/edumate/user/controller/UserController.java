package vn.base.edumate.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.CreateUserStatusHistory;
import vn.base.edumate.user.dto.request.UpdateUserRequest;
import vn.base.edumate.user.entity.UserStatus;
import vn.base.edumate.user.service.UserService;
import vn.base.edumate.user.service.UserStatusHistoryService;

import java.io.IOException;
import java.util.List;

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

        log.info("Current user: {}", userService.getCurrentUserToResponse());

        return DataResponse.<UserResponse>builder()
                .message("success")
                .data(userService.getCurrentUserToResponse())
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public DataResponse<UserResponse> updateUser(@RequestParam(value = "username",required = false) String username,
                                                    @RequestParam(value = "status", required = false) UserStatusCode status,
                                                 @RequestPart(value = "multipartFiles", required = false) MultipartFile multipartFiles) throws IOException {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username(username)
                .status(status)
                .file(multipartFiles)

                .build();
        UserResponse userResponse = userService.updateUser(updateUserRequest);
        log.info("username", userResponse.getUsername());
        return DataResponse.<UserResponse>builder()
                .message("Cập nhật thông tin người dùng thành công")
                .data(userResponse)
                .build();
    }

    @GetMapping
    public DataResponse<List<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) AuthMethod authMethod,
            @RequestParam(required = false) UserStatusCode userStatus,
            @RequestParam(required = false) String keyword) {

        Page<UserResponse> userResponses = userService.getUsers(page, size, authMethod, userStatus, keyword);
        log.info("Get all users: page {}, size {}, authMethod {}, userStatus {}", page, size, authMethod, userStatus);
        return DataResponse.<List<UserResponse>>builder()
                .message("Lấy danh sách người dùng thành công")
                .data(userResponses.getContent())
                .build();
    }

}
