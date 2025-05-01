package vn.base.edumate.post;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toModel(CreatePostRequest request);
    PostResponse toResponse(Post post);
}
