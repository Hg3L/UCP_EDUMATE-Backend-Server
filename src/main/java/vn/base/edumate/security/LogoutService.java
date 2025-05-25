package vn.base.edumate.security;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.security.jwt.JwtService;
import vn.base.edumate.token.Token;
import vn.base.edumate.token.TokenService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;

    private final JwtService jwtService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        log.info("----------------- Jwt Logout Handler -----------------");

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            log.info("Invalid Jwt header");
            return;
        }

        final String token = header.substring("Bearer ".length());
        log.info("Token: {}", token);

        final String uid = jwtService.extractIdentifier(token, TokenType.ACCESS_TOKEN);

        List<Token> storedTokens = tokenService.getAllValidTokenByUserId(uid);

        if (storedTokens != null) {
            storedTokens.forEach(t -> {
                t.setRevoked(true);
                t.setExpired(true);
            });
        }
        tokenService.saveAllToken(storedTokens);
        SecurityContextHolder.clearContext();
    }
}