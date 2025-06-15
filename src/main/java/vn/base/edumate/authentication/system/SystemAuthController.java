package vn.base.edumate.authentication.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.base.edumate.authentication.strategy.AuthServiceStrategy;
import vn.base.edumate.authentication.strategy.AuthStrategyContext;
import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.token.TokenResponse;

@RestController
@RequestMapping("/v1/auth/system")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SystemAuthController {

    private final AuthStrategyContext authStrategyContext;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody SystemAuthRequest request) {

        AuthServiceStrategy strategy = authStrategyContext.getAuthServiceStrategy(AuthType.SYSTEM);

        TokenResponse tokenResponse = strategy.authenticate(request);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        AuthServiceStrategy authServiceStrategy = authStrategyContext.getAuthServiceStrategy(AuthType.SYSTEM);
        TokenResponse tokenResponse = authServiceStrategy.refreshToken(request);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
