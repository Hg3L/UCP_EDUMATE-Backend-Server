package vn.base.edumate.tag;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.base.edumate.common.enums.TagType;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<List<Tag>> findByTagType(TagType tagType);
}
