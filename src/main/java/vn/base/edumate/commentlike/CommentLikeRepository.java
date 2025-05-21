package vn.base.edumate.commentlike;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.base.edumate.comment.Comment;
import vn.base.edumate.user.entity.User;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
    void deleteByUserAndComment(User user, Comment comment);
}
