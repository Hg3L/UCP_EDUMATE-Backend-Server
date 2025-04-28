package vn.base.edumate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.base.edumate.common.util.UserStatusCode;
import vn.base.edumate.user.entity.UserStatus;

import java.util.Optional;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {

    Optional<UserStatus> findByUserStatusCode(UserStatusCode userStatusCode);
}
