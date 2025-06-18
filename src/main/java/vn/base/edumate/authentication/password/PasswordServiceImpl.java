package vn.base.edumate.authentication.password;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.InvalidTokenTypeException;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.email.MailService;
import vn.base.edumate.security.jwt.JwtService;
import vn.base.edumate.token.Token;
import vn.base.edumate.token.TokenResponse;
import vn.base.edumate.token.TokenService;
import vn.base.edumate.user.service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final TokenService tokenService;

    private final UserService userService;

    private final JwtService jwtService;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Value("${system.default.client.url}")
    private String resetPasswordLink;

    @Override
    public TokenResponse sendLinkResetPassword(ForgotPasswordRequest request) {

        var user = userService.getUserByEmail(request.getEmail());

        if (user == null) {
            log.error("User not found with email: {}", request.getEmail());
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED);
        }

        if (!user.getAuthMethod().equals(AuthMethod.SYSTEM)) {
            log.error("User with email {} does not use system authentication method", request.getEmail());
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED);
        }

        String resetToken = jwtService.generateResetPasswordToken(user);

        long resetTokenExpire = jwtService
                .extractExpiration(resetToken, TokenType.RESET_PASSWORD_TOKEN)
                .getTime();

        Token token = Token.builder()
                .token(resetToken)
                .user(user)
                .tokenType(TokenType.RESET_PASSWORD_TOKEN)
                .revoked(false)
                .expired(false)
                .build();

        tokenService.saveToken(token);

        String resetLink = resetPasswordLink + "/reset-password?token=" + resetToken;

        handleEmail(user.getEmail(), resetLink, user.getUsername());

        return TokenResponse.builder()
                .resetPasswordToken(resetToken)
                .resetPasswordTokenExpiresIn(resetTokenExpire)
                .build();
    }

    @Override
    public void handleResetToken(HttpServletRequest request) {

        String token = null;
        if (request.getCookies() != null) {
            for (var c : request.getCookies()) {
                if ("reset_password_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        } else {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        Token storedToken = tokenService.getToken(token);

        if (storedToken == null || storedToken.isRevoked() || storedToken.isExpired()) {
            log.error("Invalid or expired reset password token: {}", token);
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        if (!storedToken.getTokenType().equals(TokenType.RESET_PASSWORD_TOKEN)) {
            log.error("Token type is not RESET_PASSWORD_TOKEN: {}", storedToken.getTokenType());
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        var user = storedToken.getUser();

        if (!jwtService.validateToken(token, TokenType.RESET_PASSWORD_TOKEN, user)) {
            log.error("Token validation failed for user: {}", user.getUsername());

            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenService.saveToken(storedToken);

            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, HttpServletRequest httpServletRequest) {

        String token = null;
        if (httpServletRequest.getCookies() != null) {
            for (var c : httpServletRequest.getCookies()) {
                if ("reset_password_token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        } else {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        Token storedToken = tokenService.getToken(token);

        if (storedToken == null || storedToken.isRevoked() || storedToken.isExpired()) {
            log.error("Invalid or expired reset password token: {}", token);
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        if (!storedToken.getTokenType().equals(TokenType.RESET_PASSWORD_TOKEN)) {
            log.error("Token type is not RESET_PASSWORD_TOKEN: {}", storedToken.getTokenType());
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        var user = storedToken.getUser();

        if (!jwtService.validateToken(token, TokenType.RESET_PASSWORD_TOKEN, user)) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenService.saveToken(storedToken);
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.saveUser(user);
    }

    private void handleEmail(String email, String link, String name) {
        try {
            Map<String, Object> variables = Map.of(
                    "subject", "Đặt lại mật khẩu hệ thống EDUMATE!",
                    "userName", name,
                    "resetLink", link,
                    "actionText", "Đặt lại mật khẩu");
            mailService.sendEmailWithTemplate(email, "confirm-link", variables);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }
}
