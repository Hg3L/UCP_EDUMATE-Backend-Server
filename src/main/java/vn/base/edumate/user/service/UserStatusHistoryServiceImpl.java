package vn.base.edumate.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.dto.request.CreateUserStatusHistory;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.entity.UserStatus;
import vn.base.edumate.user.entity.UserStatusHistory;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.repository.UserStatusHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusHistoryServiceImpl implements UserStatusHistoryService {

    private final UserStatusHistoryRepository userStatusHistoryRepository;
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Integer saveUserStatusHistoryIfUserViolation(
            String userId, CreateUserStatusHistory createUserStatusHistory) {

        User user = userService.getUserById(userId);
        int newViolationCount = user.getViolationCount() + 1;
        user.setViolationCount(newViolationCount);
        /*
        kiểm tra xem người dùng đã từng vi phạm trong lịch sử chưa
        */
        boolean hasAnyViolation = userStatusHistoryRepository.existsByUser(user);
        UserStatusCode newStatus;
        String reason;
        /*
        nếu vi phạm ở mức cảnh cáo quá 3 lần thì đặt trạng thái warning cho user
         */
        if (newViolationCount > 3) {
            newStatus = createUserStatusHistory.getStatusCode();
            reason = createUserStatusHistory.getReason();
            /*
            nếu trạng thái của người dùng đang lớn hơn mức xét của AI thì tăng mức cảnh cáo người dùng lên một bậc
            	*/
            if (hasAnyViolation && user.getStatus().ordinal() >= newStatus.ordinal()) {
                newStatus = upgradeWarningLevel(user.getStatus());
            }
            UserStatus userStatus = userStatusService.getUserStatusByStatusCode(newStatus);
            user.setExpiredAt(LocalDateTime.now().plusDays(newStatus.getDurationInDays()));
            userStatusHistoryRepository.save(UserStatusHistory.builder()
                    .user(user)
                    .userStatus(userStatus)
                    .reason(reason != null ? reason : " ")
                    .build());
            user.setStatus(newStatus);
        }
        userRepository.save(user);
        return newViolationCount;
    }

    public UserStatusCode upgradeWarningLevel(UserStatusCode current) {
        return switch (current) {
            case WARNING_LOW -> UserStatusCode.WARNING_MEDIUM;
            case WARNING_MEDIUM -> UserStatusCode.WARNING_HIGH;
            case WARNING_HIGH -> UserStatusCode.LOCKED;
            default -> current;
        };
    }
}
