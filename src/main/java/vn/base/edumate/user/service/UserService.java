package vn.base.edumate.user.service;

import com.google.firebase.auth.FirebaseToken;

import vn.base.edumate.user.entity.User;

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

    User getUserByEmail(String email);

}
