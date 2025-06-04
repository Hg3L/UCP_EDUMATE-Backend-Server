package vn.base.edumate.post;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.base.edumate.postlike.PostLikeRepository;
import vn.base.edumate.user.entity.User;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toModel(CreatePostRequest request);

    PostResponse toResponse(Post post);
    PostResponse toResponse(Post post, @Context User user, @Context PostLikeRepository repo);

    void updatePost(@MappingTarget Post post, CreatePostRequest request);
    @AfterMapping
    default void setLiked(@MappingTarget PostResponse response,
                          Post post,
                          @Context User user,
                          @Context PostLikeRepository postLikeRepository) {
        response.setLiked(postLikeRepository.existsByUserAndPost(user, post));
    }
}
