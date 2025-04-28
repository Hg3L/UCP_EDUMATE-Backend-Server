package vn.base.edumate.security.jwt;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.base.edumate.common.util.TokenType;
import vn.base.edumate.security.CustomUserDetailsService;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("----------------- Jwt Authentication Filter -----------------");

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            log.info("Invalid Jwt header");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring("Bearer ".length());
        log.info("Token: {}", token);

        final String uid = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        if(StringUtils.isNotEmpty(uid) && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = customUserDetailsService.loadUserByUsername(uid);

            if(jwtService.validateToken(token, TokenType.ACCESS_TOKEN, userDetails)) {

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }
}
