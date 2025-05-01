package vn.base.edumate.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.InvalidTokenTypeException;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.FirebaseProvider;
import vn.base.edumate.common.util.RoleCode;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.email.MailService;
import vn.base.edumate.role.Role;
import vn.base.edumate.role.RoleService;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.entity.UserStatus;
import vn.base.edumate.user.entity.UserStatusHistory;
import vn.base.edumate.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserStatusService userStatusService;

    private final RoleService roleService;

    @Transactional
    @Override
    public User createUserFromFirebase(FirebaseToken decodedToken) {
        Role userRole = roleService.getRoleByRoleType(RoleCode.USER);

        UserStatus userStatus = userStatusService.getUserStatusByStatusCode(UserStatusCode.NORMAL);

        User newUser = User.builder()
                .id(decodedToken.getUid())
                .avatarUrl(decodedToken.getPicture())
                .email(decodedToken.getEmail())
                .username(decodedToken.getName())
                .role(userRole)
                .build();

        String provider = extractSignInProvider(decodedToken);
        newUser = matchUserByProvider(newUser, provider);

        List<UserStatusHistory> userStatusHistories = List.of(
                UserStatusHistory.builder().user(newUser).userStatus(userStatus).build());
        newUser.setStatusHistories(userStatusHistories);
        log.info("Creating new user {}", newUser);
        handleEmail(newUser.getEmail(), newUser.getUsername());
        return userRepository.save(newUser);
    }

    private String extractSignInProvider(FirebaseToken token) {
        Map<String, Object> firebaseMap =
                (Map<String, Object>) token.getClaims().get("firebase");

        if (firebaseMap == null) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_FIREBASE_TOKEN);
        }

        Object providerRaw = firebaseMap.get("sign_in_provider");

        if (providerRaw == null) {
            throw new InvalidTokenTypeException(ErrorCode.INVALID_FIREBASE_TOKEN);
        }

        return providerRaw.toString();
    }

    private User matchUserByProvider(User user, String provider) {
        return FirebaseProvider.from(provider)
                .map(p -> {
                    switch (p) {
                        case GOOGLE -> user.setAuthMethod(AuthMethod.FIREBASE_GOOGLE);
                        case FACEBOOK -> user.setAuthMethod(AuthMethod.FIREBASE_FACEBOOK);
                    }
                    return user;
                })
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AUTH_PROVIDER_NOT_FOUND));
    }

    private void handleEmail(String email, String name) {
        try {
            Map<String, Object> variables = Map.of("subject", "EDUMATE Xin Chào!", "userName", name);
            mailService.sendEmailWithTemplate(email, "welcome", variables);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }

    @Override
    public User getUserById(String userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not found with email: {}", email);
            return new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED);
        });
    }
}
