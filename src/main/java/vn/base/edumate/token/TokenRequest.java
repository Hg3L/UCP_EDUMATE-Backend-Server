package vn.base.edumate.token;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequest {

    @NotBlank
    private String token;
}
