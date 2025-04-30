package vn.base.edumate.user.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.base.edumate.user.entity.UserStatusHistory;
import vn.base.edumate.user.repository.UserStatusHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusHistoryServiceImpl implements UserStatusHistoryService {

    private final UserStatusHistoryRepository userStatusHistoryRepository;

    @Override
    public void saveUserStatusHistory(UserStatusHistory userStatusHistory) {
        userStatusHistoryRepository.save(userStatusHistory);
    }
}
