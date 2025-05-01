package vn.base.edumate.authentication.firebase;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.authentication.strategy.AuthServiceStrategy;
import vn.base.edumate.common.exception.AccountLockedException;
import vn.base.edumate.common.exception.CustomFirebaseAuthException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.InvalidTokenTypeException;
import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.security.jwt.JwtService;
import vn.base.edumate.token.TokenRequest;
import vn.base.edumate.token.TokenResponse;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseAuthServiceStrategy implements AuthServiceStrategy {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Override
    public boolean support(AuthType authType) {
        return authType == AuthType.FIREBASE;
    }

    @Override
    public TokenResponse authenticate(Object o) {
        TokenRequest request = (TokenRequest) o;

        String token = request.getToken();

        if (StringUtils.isBlank(token)) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_FIREBASE_TOKEN);
        }

        User user;
        List<String> authorities;

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            user = userService.getUserById(decodedToken.getUid());

            if (user == null) {
                user = userService.createUserFromFirebase(decodedToken);
            }

            authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            log.info("Verifying id {}", decodedToken.getUid());
            log.info("Picture link {}", decodedToken.getPicture());

        } catch (FirebaseAuthException e) {
            throw mapFirebaseAuthException(e);
        }

        String accessToken = jwtService.generateAccessToken(user, authorities);
        String refreshToken = jwtService.generateRefreshToken(user, authorities);
        long accessTokenExpiresIn = jwtService
                .extractExpiration(accessToken, TokenType.ACCESS_TOKEN)
                .getTime();
        long refreshTokenExpiresIn = jwtService
                .extractExpiration(refreshToken, TokenType.REFRESH_TOKEN)
                .getTime();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }

    @Override
    public TokenResponse refreshToken(Object o) {
        TokenRequest request = (TokenRequest) o;

        String refreshToken = request.getToken();

        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        var user = userDetailsService.loadUserByUsername(
                jwtService.extractIdentifier(refreshToken, TokenType.REFRESH_TOKEN));

        if (!jwtService.validateToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        if (!user.isAccountNonLocked()) {
            throw new AccountLockedException(ErrorCode.USER_LOCKED);
        }

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtService.generateAccessToken(user, authorities);
        long accessTokenExpiresIn = jwtService
                .extractExpiration(accessToken, TokenType.ACCESS_TOKEN)
                .getTime();
        long refreshTokenExpiresIn = jwtService
                .extractExpiration(refreshToken, TokenType.REFRESH_TOKEN)
                .getTime();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }

    /**
     * Map FirebaseAuthException from Google Firebase to CustomFirebaseAuthException
     */
    private CustomFirebaseAuthException mapFirebaseAuthException(FirebaseAuthException e) {
        AuthErrorCode code = e.getAuthErrorCode();

        return switch (code) {
            case EXPIRED_ID_TOKEN -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Token đã hết hạn");
            case INVALID_ID_TOKEN -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Token không hợp lệ");
            case REVOKED_ID_TOKEN -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Token đã bị thu hồi");
            case EXPIRED_SESSION_COOKIE -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Session cookie đã hết hạn");
            case INVALID_SESSION_COOKIE -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Session cookie không hợp lệ");
            case REVOKED_SESSION_COOKIE -> new CustomFirebaseAuthException(
                    HttpStatus.UNAUTHORIZED.value(), code, "Session cookie đã bị thu hồi");
            case USER_DISABLED -> new CustomFirebaseAuthException(
                    HttpStatus.FORBIDDEN.value(), code, "Tài khoản đã bị vô hiệu hóa");
            case USER_NOT_FOUND -> new CustomFirebaseAuthException(
                    HttpStatus.NOT_FOUND.value(), code, "Không tìm thấy người dùng");
            case EMAIL_ALREADY_EXISTS -> new CustomFirebaseAuthException(
                    HttpStatus.CONFLICT.value(), code, "Email đã tồn tại");
            case EMAIL_NOT_FOUND -> new CustomFirebaseAuthException(
                    HttpStatus.NOT_FOUND.value(), code, "Không tìm thấy email");
            case PHONE_NUMBER_ALREADY_EXISTS -> new CustomFirebaseAuthException(
                    HttpStatus.CONFLICT.value(), code, "Số điện thoại đã tồn tại");
            case TENANT_ID_MISMATCH -> new CustomFirebaseAuthException(
                    HttpStatus.FORBIDDEN.value(), code, "Tenant ID không khớp");
            case TENANT_NOT_FOUND -> new CustomFirebaseAuthException(
                    HttpStatus.NOT_FOUND.value(), code, "Không tìm thấy tenant");
            case UID_ALREADY_EXISTS -> new CustomFirebaseAuthException(
                    HttpStatus.CONFLICT.value(), code, "UID đã tồn tại");
            case UNAUTHORIZED_CONTINUE_URL -> new CustomFirebaseAuthException(
                    HttpStatus.BAD_REQUEST.value(), code, "URL không được phép tiếp tục");
            case CERTIFICATE_FETCH_FAILED -> new CustomFirebaseAuthException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), code, "Lỗi khi lấy certificate từ Firebase");
            case CONFIGURATION_NOT_FOUND -> new CustomFirebaseAuthException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), code, "Không tìm thấy cấu hình Firebase");
            case INVALID_DYNAMIC_LINK_DOMAIN -> new CustomFirebaseAuthException(
                    HttpStatus.BAD_REQUEST.value(), code, "Dynamic link domain không hợp lệ");
        };
    }
}
