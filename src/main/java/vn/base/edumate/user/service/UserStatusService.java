package vn.base.edumate.user.service;

import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.entity.UserStatus;

public interface UserStatusService {
    /**
     * Used by another service
     */
    UserStatus getUserStatusByStatusCode(UserStatusCode userStatusCode);
}
