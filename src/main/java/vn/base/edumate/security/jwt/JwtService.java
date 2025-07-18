package vn.base.edumate.security.jwt;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.user.entity.User;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails, List<String> authorities);

    String generateRefreshToken(UserDetails userDetails, List<String> authorities);

    String generateResetPasswordToken(UserDetails userDetails);

    String extractIdentifier(String token, TokenType tokenType);

    Date extractExpiration(String token, TokenType tokenType);

    boolean validateToken(String token, TokenType tokenType, User userDetails);
}
