package vn.base.edumate.authentication.firebase;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.authentication.strategy.AuthServiceStrategy;
import vn.base.edumate.authentication.strategy.AuthStrategyContext;
import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.token.TokenRequest;
import vn.base.edumate.token.TokenResponse;

@RestController
@RequestMapping("/v1/auth/firebase")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FirebaseAuthController {

    private final AuthStrategyContext authStrategyContext;

    @PostMapping
    public ResponseEntity<TokenResponse> authenticate(@Valid @RequestBody TokenRequest request) {
        AuthServiceStrategy authServiceStrategy = authStrategyContext.getAuthServiceStrategy(AuthType.FIREBASE);
        TokenResponse tokenResponse = authServiceStrategy.authenticate(request);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
