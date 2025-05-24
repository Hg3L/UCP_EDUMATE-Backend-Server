package vn.base.edumate.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostId(Long postId);

    Optional<List<Comment>> findByParentId(Long parentId);
}
