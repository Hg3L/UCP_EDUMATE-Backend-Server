package vn.base.edumate.history.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.base.edumate.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AISearchRepository extends JpaRepository<AISearch, Long> {

    @Query("SELECT i FROM AISearch i WHERE i.user = :user ORDER BY i.createdAt DESC")
    List<AISearch> findAISearchByUser(User user);
}
