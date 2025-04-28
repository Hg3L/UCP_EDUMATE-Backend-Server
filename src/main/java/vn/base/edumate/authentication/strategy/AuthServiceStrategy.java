package vn.base.edumate.authentication.strategy;

import vn.base.edumate.token.TokenResponse;
import vn.base.edumate.common.util.AuthType;

public interface AuthServiceStrategy {

    boolean support(AuthType authType);

    TokenResponse authenticate(Object o);

    TokenResponse refreshToken(Object o);

}
