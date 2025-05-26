package vn.base.edumate.user.service;

import com.google.firebase.auth.FirebaseToken;

import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.repository.UserRepository;

public interface UserService {

    /**
     * Used by controller
     */

    /**
     * Used by another service
     */
    User createUserFromFirebase(FirebaseToken decodedToken);

    User getUserById(String userId);

    User getCurrentUser();
    UserResponse getCurrentUserToResponse();

    User getUserByEmail(String email);

    void saveUser(User user);
}
