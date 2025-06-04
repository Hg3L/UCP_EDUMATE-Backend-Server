package vn.base.edumate.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.base.edumate.common.util.PostStatus;
import vn.base.edumate.common.util.TagType;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByTagId(Long tagId);
    Optional<List<Post>> findByAuthorId(String authorId);

    Optional<List<Post>> findByTagTagTypeAndStatusOrderByCreatedAtDesc(TagType tagType, PostStatus status);

    Optional<Post> findByImages_Id(Long imagesId);
}
