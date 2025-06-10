package vn.base.edumate.user.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.UpdateUserRequest;
import vn.base.edumate.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    @AfterMapping
    default void calculateCounts(User user, @MappingTarget UserResponse userResponse) {
        if (user != null && userResponse != null) {
            int commentCount = user.getComments() != null ? user.getComments().size() : 0;
            int postCount = user.getPosts() != null ? user.getPosts().size() : 0;

            userResponse.setCommentCount(commentCount);
            userResponse.setPostCount(postCount);
        }
    }
}
