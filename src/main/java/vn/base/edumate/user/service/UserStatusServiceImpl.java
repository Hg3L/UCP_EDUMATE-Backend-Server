package vn.base.edumate.user.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.entity.UserStatus;
import vn.base.edumate.user.repository.UserStatusRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus getUserStatusByStatusCode(UserStatusCode userStatusCode) {
        return userStatusRepository
                .findByUserStatusCode(userStatusCode)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_STATUS_NOT_EXISTED));
    }
}
