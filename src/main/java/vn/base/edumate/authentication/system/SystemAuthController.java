package vn.base.edumate.authentication.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.base.edumate.authentication.password.ForgotPasswordRequest;
import vn.base.edumate.authentication.password.PasswordService;
import vn.base.edumate.authentication.password.ResetPasswordRequest;
import vn.base.edumate.authentication.strategy.AuthServiceStrategy;
import vn.base.edumate.authentication.strategy.AuthStrategyContext;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.token.TokenResponse;

@RestController
@RequestMapping("/v1/auth/system")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SystemAuthController {

    private final AuthStrategyContext authStrategyContext;

    private final PasswordService passwordService;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request)  {

        String response = passwordService.sendLinkResetPassword(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public DataResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request, HttpServletRequest httpServletRequest) {

        passwordService.resetPassword(request, httpServletRequest);

        return DataResponse.<Void>builder()
                .message("Change password successfully")
                .build();
    }

    @GetMapping("/check-token")
    public DataResponse<Void> checkToken(HttpServletRequest request) {

        passwordService.handleResetToken(request);

        return DataResponse.<Void>builder()
                .message("Token is valid")
                .build();

    }
}
