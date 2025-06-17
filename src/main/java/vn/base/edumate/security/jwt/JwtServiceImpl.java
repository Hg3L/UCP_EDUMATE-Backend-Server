package vn.base.edumate.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.InvalidTokenTypeException;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.user.entity.User;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${system.security.jwt.access.expiration-hour}")
    private long accessExpiration;

    @Value("${system.security.jwt.refresh.expiration-day}")
    private long refreshExpiration;

    @Value("${system.security.jwt.reset-password.expiration-minute}")
    private long resetPasswordExpiration;

    @Value("${system.security.jwt.secretAccessKey}")
    private String secretAccessKey;

    @Value("${system.security.jwt.secretRefreshKey}")
    private String secretRefreshKey;

    @Value("${system.security.jwt.secretResetPasswordKey}")
    private String secretResetPasswordKey;

    @Override
    public String generateAccessToken(UserDetails userDetails, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        return generateToken(claims, userDetails, TokenType.ACCESS_TOKEN);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        return generateToken(claims, userDetails, TokenType.REFRESH_TOKEN);
    }

    @Override
    public String generateResetPasswordToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, TokenType.RESET_PASSWORD_TOKEN);
    }

    @Override
    public String extractIdentifier(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, TokenType tokenType, User userDetails) {

        final String username = extractIdentifier(token, tokenType);
        return username.equals(userDetails.getId()) && !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    @Override
    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, TokenType tokenType) {
        String userId = ((User) userDetails).getId();
        return switch (tokenType) {
            case ACCESS_TOKEN -> Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * accessExpiration))
                    .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                    .compact();
            case REFRESH_TOKEN -> Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * refreshExpiration))
                    .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                    .compact();

            case RESET_PASSWORD_TOKEN -> Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * resetPasswordExpiration))
                    .signWith(getKey(TokenType.RESET_PASSWORD_TOKEN), SignatureAlgorithm.HS256)
                    .compact();
        };
    }

    private Key getKey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretRefreshKey));
            }
            case RESET_PASSWORD_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretResetPasswordKey));
            }
            default -> throw new InvalidTokenTypeException(ErrorCode.INVALID_TOKEN_TYPE);
        }
    }

    private <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType tokenType) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey(tokenType))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
