package vn.base.edumate.user.service;

import com.google.firebase.auth.FirebaseToken;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import vn.base.edumate.common.util.AuthMethod;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.UpdateAccountRequest;
import vn.base.edumate.user.dto.request.UpdateUserRequest;
import vn.base.edumate.user.entity.User;

import java.io.IOException;

public interface UserService {

    /**
     * Used by controller
     */
    Page<UserResponse> getUsers(int page,
                                int size,
                                @Nullable AuthMethod authMethod,
                                @Nullable UserStatusCode userStatusCode,
                                @Nullable String keyword);

    void setUserStatus(UpdateAccountRequest request);

    Page<UserResponse> getDeletedUsers(int page,
                                       int size,
                                       @Nullable AuthMethod authMethod,
                                       @Nullable String keyword);

    /**
     * Used by another service
     */
    User createUserFromFirebase(FirebaseToken decodedToken);

    User getUserById(String userId);

    User getCurrentUser();

    UserResponse getCurrentUserToResponse();

    User getUserByEmail(String email);

    void saveUser(User user);

    UserResponse updateUser(UpdateUserRequest updateUserRequest) throws IOException;

}
