package vn.base.edumate.authentication.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.util.AuthType;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthStrategyContext {

    private final List<AuthServiceStrategy> strategies;

    public AuthServiceStrategy getAuthServiceStrategy(AuthType authType) {
        return strategies.stream()
                .filter(s -> s.support(authType))
                .findFirst()
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.INVALID_ARGUMENT));
    }
}
