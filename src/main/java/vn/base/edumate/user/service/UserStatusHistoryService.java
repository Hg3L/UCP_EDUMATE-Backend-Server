package vn.base.edumate.user.service;

import vn.base.edumate.user.dto.request.CreateUserStatusHistory;

public interface UserStatusHistoryService {
    /**
     * Used by another service
     */
    Integer saveUserStatusHistoryIfUserViolation(String userId, CreateUserStatusHistory createUserStatusHistory);
}
