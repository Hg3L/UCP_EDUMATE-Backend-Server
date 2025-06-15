package vn.base.edumate.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.base.edumate.common.util.PostStatus;
import vn.base.edumate.common.util.TagType;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByTagIdAndStatus(Long tagId,PostStatus status);
    Optional<List<Post>> findByAuthorIdAndStatus(String authorId,PostStatus status);

    Optional<List<Post>> findByTagTagTypeAndStatusOrderByCreatedAtDesc(Pageable pageable, TagType tagType, PostStatus status);

    Optional<Post> findByImages_Id(Long imagesId);
    Integer countByTagTagTypeAndStatus(TagType tagType, PostStatus status);
    Optional<List<Post>> findAllByStatus(PostStatus status);
}
