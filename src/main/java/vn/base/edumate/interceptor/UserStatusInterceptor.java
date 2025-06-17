package vn.base.edumate.interceptor;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.security.jwt.JwtService;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.service.UserService;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStatusInterceptor implements HandlerInterceptor {

    UserService userService;
    JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userId = jwtService.extractIdentifier(token, TokenType.ACCESS_TOKEN);

            User user = userService.getUserById(userId);
            UserStatusCode userStatus = user.getStatus();
            String requestURI = request.getRequestURI();
            // Xử lý riêng cho endpoint /login
            if (requestURI.equals("/v1/auth/firebase")) {
                if (user.getStatus() == UserStatusCode.LOCKED) {
                    sendLockedResponse(response);
                    return false;
                }
                return true;
            }
            if (userStatus == UserStatusCode.LOCKED) {
                response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter()
                        .write(
                                """
				{
				"code": 403,
				"message": "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ hỗ trợ."
				}
			""");
                return false;
            }
            /*
            Nếu là warning thì kiểm tra thời hạn
            */
            if (user.getExpiredAt() != null) {

                LocalDateTime expiredAt = user.getExpiredAt();
                // Nếu còn trong thời hạn cảnh báo → chặn luôn
                if (LocalDateTime.now().isBefore(expiredAt)) {
                    Duration remaining = Duration.between(LocalDateTime.now(), expiredAt);

                    long days = remaining.toDays();
                    long hours = remaining.toHours() % 24;
                    long minutes = remaining.toMinutes() % 60;

                    String remainingStr = String.format("%d ngày, %d giờ, %d phút", days, hours, minutes);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter()
                            .write(String.format(
                                    """
					{
					"code": 403,
					"message": "Tài khoản đang bị cảnh báo (%s). Vui lòng chờ đến khi hết hạn(còn lại: %s) hoặc liên hệ hỗ trợ."
					}
					""",
                                    userStatus.getDescription(), remainingStr));
                    return false;
                }
                /*
                nếu hết hạn thì reset lại
                */
                if (LocalDateTime.now().isAfter(expiredAt)) {
                    user.setStatus(UserStatusCode.NORMAL);
                    user.setExpiredAt(null);
                    userService.saveUser(user);
                }
            }
        }

        return true;
    }
    private void sendLockedResponse(HttpServletResponse response) throws IOException, IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("""
            {
                "code": 403,
                "message": "Tài khoản đã bị khóa vĩnh viễn"
            }
            """);
    }
}
