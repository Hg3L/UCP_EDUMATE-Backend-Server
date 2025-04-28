package vn.base.edumate.user.service;

import vn.base.edumate.user.entity.UserStatusHistory;

public interface UserStatusHistoryService {
    /**
     * Used by another service
     */
    void saveUserStatusHistory(UserStatusHistory userStatusHistory);
}
