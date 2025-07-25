package vn.base.edumate.tag;

import java.util.List;

import vn.base.edumate.common.util.TagType;

public interface TagService {
    List<TagResponse> getAllTags();

    List<TagResponse> getTagByType(TagType type);

    TagResponse getTagById(Long id);

    TagResponse createTag(CreateTagRequest createTagRequest);
}
