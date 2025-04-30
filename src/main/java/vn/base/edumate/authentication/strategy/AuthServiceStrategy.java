package vn.base.edumate.authentication.strategy;

import vn.base.edumate.common.util.AuthType;
import vn.base.edumate.token.TokenResponse;

public interface AuthServiceStrategy {

    boolean support(AuthType authType);

    TokenResponse authenticate(Object o);

    TokenResponse refreshToken(Object o);
}
