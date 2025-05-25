package vn.base.edumate.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.entity.UserStatusHistory;

@Repository
public interface UserStatusHistoryRepository extends JpaRepository<UserStatusHistory, Integer> {

    boolean existsByUser(User user);
}
