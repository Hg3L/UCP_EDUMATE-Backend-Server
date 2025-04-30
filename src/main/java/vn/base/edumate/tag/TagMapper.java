package vn.base.edumate.tag;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toModel(CreateTagRequest tag);

    TagResponse toResponse(Tag tag);
}
