package vn.base.edumate.post_report;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.base.edumate.post.Post;
import vn.base.edumate.user.entity.User;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    Boolean existsByUserAndPost(User user, Post post);
    long countByPost(Post post);
}
