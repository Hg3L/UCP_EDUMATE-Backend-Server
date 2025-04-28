package vn.base.edumate.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import vn.base.edumate.common.util.TokenType;

import java.util.Date;
import java.util.List;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails, List<String> authorities);

    String generateRefreshToken(UserDetails userDetails, List<String> authorities);

    String extractUsername(String token, TokenType tokenType);

    Date extractExpiration(String token, TokenType tokenType);

    boolean validateToken(String token, TokenType tokenType, UserDetails userDetails);
}
