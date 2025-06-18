package vn.base.edumate.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {

    String accessToken;

    String refreshToken;

    String resetPasswordToken;

    long accessTokenExpiresIn;

    long refreshTokenExpiresIn;

    long resetPasswordTokenExpiresIn;
}
