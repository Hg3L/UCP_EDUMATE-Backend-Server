package vn.base.edumate.postlike;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.base.edumate.post.Post;
import vn.base.edumate.user.entity.User;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    void deleteByPost(Post post); // Xóa toàn bộ like khi xóa post
}
