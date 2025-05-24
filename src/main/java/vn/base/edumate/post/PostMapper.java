package vn.base.edumate.post;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toModel(CreatePostRequest request);

    PostResponse toResponse(Post post);

    void updatePost(@MappingTarget Post post, CreatePostRequest request);
}
