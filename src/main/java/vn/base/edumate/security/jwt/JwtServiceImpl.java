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

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${system.security.jwt.access.expiration-hour}")
    private long accessExpiration;

    @Value("${system.security.jwt.refresh.expiration-day}")
    private long refreshExpiration;

    @Value("${system.security.jwt.secretAccessKey}")
    private String secretAccessKey;

    @Value("${system.security.jwt.secretRefreshKey}")
    private String secretRefreshKey;

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
    public String extractUsername(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, TokenType tokenType, UserDetails userDetails) {
        final String username = extractUsername(token, tokenType);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    @Override
    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, TokenType tokenType) {
        return switch (tokenType) {
            case ACCESS_TOKEN -> Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * accessExpiration))
                    .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                    .compact();
            case REFRESH_TOKEN -> Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * refreshExpiration))
                    .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
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
