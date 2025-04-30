package vn.base.edumate.tag;

import java.util.List;

import vn.base.edumate.common.enums.TagType;

public interface TagService {
    List<TagResponse> getAllTags();

    List<TagResponse> getTagByType(TagType type);

    TagResponse createTag(CreateTagRequest createTagRequest);
}
