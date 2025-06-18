package vn.base.edumate.authentication.system;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.base.edumate.authentication.strategy.AuthServiceStrategy;
import vn.base.edumate.common.exception.AccountLockedException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.InvalidTokenTypeException;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.security.jwt.JwtService;
import vn.base.edumate.token.Token;
import vn.base.edumate.token.TokenResponse;
import vn.base.edumate.token.TokenService;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemAuthServiceStrategy implements AuthServiceStrategy {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    @Override
    public boolean support(AuthType authType) {
        return authType == AuthType.SYSTEM;
    }

    @Override
    public TokenResponse authenticate(Object o) {

        SystemAuthRequest request = (SystemAuthRequest) o;

        log.info("---------- System Authenticate ----------");
        log.info("---------- Authenticate ----------");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        log.info("Is authenticated: {}", authentication.isAuthenticated());
        log.info("Authorities: {}", authentication.getAuthorities().toString());

        User user = userService.getUserByEmail(request.getEmail());

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtService.generateAccessToken(user, authorities);
        String refreshToken = jwtService.generateRefreshToken(user, authorities);
        long accessTokenExpiresIn = jwtService
                .extractExpiration(accessToken, TokenType.ACCESS_TOKEN)
                .getTime();
        long refreshTokenExpiresIn = jwtService
                .extractExpiration(refreshToken, TokenType.REFRESH_TOKEN)
                .getTime();

        Token accessTokenDb = Token.builder()
                .token(accessToken)
                .tokenType(TokenType.ACCESS_TOKEN)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        Token refreshTokenDb = Token.builder()
                .token(refreshToken)
                .tokenType(TokenType.REFRESH_TOKEN)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        tokenService.saveAllToken(List.of(accessTokenDb, refreshTokenDb));
        log.info("Token saved successfully");

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .build();
    }

    @Override
    public TokenResponse refreshToken(Object o) {
        HttpServletRequest request = (HttpServletRequest) o;

        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            log.error("Refresh token is missing or blank in cookie");
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        String uid = jwtService.extractIdentifier(refreshToken, TokenType.REFRESH_TOKEN);

        if (uid == null || StringUtils.isBlank(uid)) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        var user = (User) userDetailsService.loadUserByUsername(uid);

        if (!jwtService.validateToken(refreshToken, TokenType.REFRESH_TOKEN, user)) {
            var rToken = tokenService.getToken(refreshToken);
            rToken.setExpired(true);
            tokenService.saveToken(rToken);
            throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN);
        }

        if (!user.isAccountNonLocked()) {
            throw new AccountLockedException(ErrorCode.USER_LOCKED);
        }

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtService.generateAccessToken(user, authorities);

        tokenService.saveToken(Token.builder()
                .token(accessToken)
                .tokenType(TokenType.ACCESS_TOKEN)
                .revoked(false)
                .expired(false)
                .build());

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

}
