package vn.base.edumate.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * Firebase error codes
     */
    INVALID_FIREBASE_TOKEN(1001, HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ"),
    AUTH_PROVIDER_NOT_FOUND(1002, HttpStatus.UNAUTHORIZED.value(), "Không tìm thấy nhà cung cấp xác thực hợp lệ"),

    /**
     * Jwt error codes
     */
    INVALID_TOKEN(1003, HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ"),
    INVALID_TOKEN_TYPE(1004, HttpStatus.UNAUTHORIZED.value(), "Loại Token không hợp lệ"),
    EXPIRED_TOKEN(1005, HttpStatus.UNAUTHORIZED.value(), "Token hết hạn"),

    /**
     * Authentication error codes
     */
    UNAUTHORIZED(1006, HttpStatus.UNAUTHORIZED.value(), "Người dùng không có quyền truy cập"),
    UNAUTHENTICATED(1007, HttpStatus.UNAUTHORIZED.value(), "Người dùng chưa xác thực"),


    /**
     * Mail error codes
     */
    MAIL_SENDER_NOT_FOUND(1008, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Không tìm thấy MailSender"),

    /**
     * User error codes
     */
    USER_NOT_EXISTED(1009, HttpStatus.NOT_FOUND.value(), "Người dùng không tồn tại"),
    USER_STATUS_NOT_EXISTED(1010, HttpStatus.NOT_FOUND.value(), "Trạng thái người dùng không tồn tại"),
    USER_LOCKED(1011, HttpStatus.FORBIDDEN.value(), "Tài khoản đã bị khóa"),

    /**
     * RoleCode error codes
     */
    ROLE_NOT_EXISTED(1012, HttpStatus.NOT_FOUND.value(), "Vai trò không tồn tại"),

    /**
     * General error codes
     */
    INVALID_ARGUMENT(1013, HttpStatus.BAD_REQUEST.value(), "Tham số không hợp lệ"),
    UNCATEGORIZED(9999, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi không xác định"),

    /**
     * Tag error codes
     */
    TAG_NOT_EXISTED(1015, HttpStatus.NOT_FOUND.value(), "Tag không tồn tại"),
    TAG_TYPE_NOT_EXISTED(1016, HttpStatus.NOT_FOUND.value(), "Loại tag này không tồn tại"),
    /**
     * Post error codes
     */
    POST_NOT_EXISTED(1016,HttpStatus.NOT_FOUND.value(), "Bài viết không tồn tại"),
    /**
     * Image error codes
     */
    IMAGE_NOT_EXISTED(1017,HttpStatus.NOT_FOUND.value(), "Ảnh không tồn tại"),
    /**
     * Comment error codes
     */
    COMMENT_NOT_EXISTED(1018,HttpStatus.NOT_FOUND.value(), "Comment không tồn tại"),
    /**
     * Report error codes
     */
    REPORT_EXISTED_BY_USER (1019,HttpStatus.CONFLICT.value(), "Bạn đã tố cáo bài viết này"),
    /**
     * Report error codes
     */
    HISTORY_NOT_EXISTED(1020, HttpStatus.NOT_FOUND.value(), "Lịch sử không tồn tại");

    private final int code;
    private final int status;
    private final String message;
}
