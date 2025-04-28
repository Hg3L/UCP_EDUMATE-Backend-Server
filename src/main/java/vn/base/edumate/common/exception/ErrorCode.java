package vn.base.edumate.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Firebase error codes
     */
    INVALID_FIREBASE_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ"),
    AUTH_PROVIDER_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "Không tìm thấy nhà cung cấp xác thực hợp lệ"),
    /**
     * Jwt error codes
     */
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Token không hợp lệ"),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED.value(), "Loại Token không hợp lệ"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Token hết hạn"),
    /**
     * Authentication error codes
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "Người dùng không có quyền truy cập"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED.value(), "Người dùng chưa xác thực"),
    /**
     * Mail error codes
     */
    MAIL_SENDER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Không tìm thấy MailSender"),
    /**
     * User error codes
     */
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND.value(), "Người dùng không tồn tại"),
    USER_STATUS_NOT_EXISTED(HttpStatus.NOT_FOUND.value(), "Trạng thái người dùng không tồn tại"),
    USER_LOCKED(HttpStatus.FORBIDDEN.value(), "Tài khoản đã bị khóa"),
    /**
     * RoleCode error codes
     */
    ROLE_NOT_EXISTED(HttpStatus.NOT_FOUND.value(), "Vai trò không tồn tại"),
    /**
     * General error codes (Code skill)
     */
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "Tham số không hợp lệ"),
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi không xác định");

    private final int status;
    private final String message;
}
