package vn.base.edumate.common.util;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FirebaseProvider {
    GOOGLE("google.com"),
    FACEBOOK("facebook.com");

    private final String provider;

    public static Optional<FirebaseProvider> from(String provider) {
        return Arrays.stream(values()).filter(p -> p.provider.equals(provider)).findFirst();
    }
}
