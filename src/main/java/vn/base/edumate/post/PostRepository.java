package vn.base.edumate.post;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.base.edumate.common.util.TagType;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>>  findByTagId(Long tagId);
    Optional<List<Post>> findByTagTagType(TagType tagType);
}
