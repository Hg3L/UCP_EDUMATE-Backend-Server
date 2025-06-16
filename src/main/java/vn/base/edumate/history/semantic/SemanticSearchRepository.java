package vn.base.edumate.history.semantic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.base.edumate.user.entity.User;

import java.util.List;

@Repository
public interface SemanticSearchRepository extends JpaRepository<SemanticSearch, Long> {

    @Query("SELECT s FROM SemanticSearch s WHERE s.user = :user ORDER BY s.createdAt DESC")
    List<SemanticSearch> findBySemanticSearchByUser(User user);

    void deleteAllByUser(User user);
}
